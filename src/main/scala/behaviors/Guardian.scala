package behaviors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import protocol.message.{Message, Request, Response}
import protocol.status.{Fatal, Ready, Starting, Status}

/**
  * @author kasonchan
  * @since 2020-03-21
  */
object Guardian {
  def apply(): Behavior[Message] = status(Starting)

  private def status(currentStatus: Status): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      currentStatus match {
        case Starting =>
          Behaviors
            .receiveMessage {
              case Request(protocol.command.Activate, replyTo) =>
                context.log.info("[{}]", Starting.toString.toUpperCase)
                val web = context.spawn(Web(), "web")
                web ! Request(protocol.command.Activate, context.self)
                context.log
                  .info(
                    "[{}]-{}->[{}]",
                    currentStatus.toString.toUpperCase,
                    protocol.command.Activate.toString,
                    Ready.toString.toUpperCase
                  )
                replyTo ! Response(currentStatus)
                status(currentStatus)
              case Request(protocol.command.Status, replyTo) =>
                replyTo ! Response(currentStatus)
                status(currentStatus)
              case Response(responseStatus) =>
                context.log
                  .info("[{}]", responseStatus.toString.toUpperCase)
                status(responseStatus)
            }
        case _ =>
          Behaviors
            .receiveMessage {
              case Request(protocol.command.Status, replyTo) =>
                replyTo ! Response(currentStatus)
                status(currentStatus)
              case _ =>
                status(currentStatus)
            }
      }
    }
}
