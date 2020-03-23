package behaviors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import app.Profile.{serviceHost, servicePort}
import http.HttpService.{routes, start}
import protocol.command.Activate
import protocol.message.{Message, Request, Response}
import protocol.status.{Fatal, Ready, Starting, Status}

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since 2020-03-21
  */
object Web {
  def apply(): Behavior[Message] = status(Starting)

  private def status(currentStatus: Status): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      currentStatus match {
        case Starting =>
          context.log.info("[{}]", Starting.toString.toUpperCase)
          Behaviors
            .receiveMessage {
              case Request(command, replyTo) =>
                command match {
                  case Activate =>
                    context.log.info(
                      "[{}]-{}->[{}]",
                      currentStatus.toString.toUpperCase,
                      Activate.toString,
                      Ready.toString.toUpperCase
                    )
                    val futureBinding: Future[Http.ServerBinding] =
                      start(serviceHost, servicePort, routes, context.system)
                    import scala.concurrent.duration._
                    Try {
                      Await.result(futureBinding, 1.minute)
                    } match {
                      case Success(v) =>
                        context.log
                          .info("Successfully bounded to {}", v.localAddress)
                        replyTo ! Response(Ready)
                        context.log.info("[{}]", Ready.toString.toUpperCase)
                        status(Ready)
                      case Failure(ex) =>
                        context.log.error(
                          "[{}]",
                          Fatal.toString.toUpperCase,
                          ex.getMessage
                        )
                        status(Fatal)
                    }
                  case _ =>
                    status(currentStatus)
                }
              case _ =>
                status(currentStatus)
            }
        case _ =>
          status(currentStatus)
      }
    }
}
