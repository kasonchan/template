package behaviors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import app.Profile.{bindingTimeoutInMilliseconds, serviceHost, servicePort}
import http.HttpService
import protocol.command.{Activate, Status}
import protocol.message.{Message, Request, Response}
import protocol.status.{Fatal, Ready, Starting, Status}

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since 2020-03-21
  */
object Web {
  def apply(): Behavior[Message] = starting()

  private val httpServer = HttpService

  private def starting(
      currentStatus: Status = Starting,
      serverBinding: Option[ServerBinding] = None
  ): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      Behaviors.receiveMessage {
        case Request(Activate, replyTo) =>
          context.log.info("[{}]", currentStatus.toString.toUpperCase)
          val futureBinding: Future[Http.ServerBinding] =
            HttpService.start(
              serviceHost,
              servicePort,
              httpServer.routes,
              context.system
            )
          context.log.info(
            "[{}]-{}->[{}]",
            currentStatus.toString.toUpperCase,
            Activate.toString,
            Ready.toString.toUpperCase
          )
          Try {
            Await.result(futureBinding, bindingTimeoutInMilliseconds)
          } match {
            case Success(v) =>
              context.log.info("Successfully bounded to {}", v.localAddress)
              replyTo ! Response(Ready)
              context.log.info("[{}]", Ready.toString.toUpperCase)
              ready(serverBinding = Some(v))
            case Failure(ex) =>
              context.log.error(
                "[{}]",
                Fatal.toString.toUpperCase,
                ex.getMessage
              )
              replyTo ! Response(Fatal)
              context.log.info("[{}]", Fatal.toString.toUpperCase)
              fatal(serverBinding = None)
          }
        case Request(Status, replyTo) =>
          replyTo ! Response(currentStatus)
          context.log.info("[{}]", Fatal.toString.toUpperCase)
          Behaviors.same
        case message: Message =>
          context.log.debug("{}", message)
          Behaviors.same
      }
    }

  private def ready(
      currentStatus: Status = Ready,
      serverBinding: Option[ServerBinding]
  ): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      Behaviors.receiveMessage {
        case Request(Status, replyTo) =>
          replyTo ! Response(currentStatus)
          Behaviors.same
        case message: Message =>
          context.log.debug("{}", message)
          Behaviors.same
      }
    }

  private def fatal(
      currentStatus: Status = Fatal,
      serverBinding: Option[ServerBinding]
  ): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      Behaviors.receiveMessage {
        case Request(Status, replyTo) =>
          replyTo ! Response(currentStatus)
          Behaviors.same
        case message: Message =>
          context.log.debug("{}", message)
          Behaviors.same
      }
    }
}
