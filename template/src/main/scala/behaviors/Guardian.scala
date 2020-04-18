package behaviors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ Behavior, DispatcherSelector }
import protocol.message.{ Message, Request, Response }
import protocol.status.{ Fatal, Ready, Starting, Status }

/**
 * @author kasonchan
 * @since 2020-03-21
 */
object Guardian {
  def apply(): Behavior[Message] = starting()

  private def starting(currentStatus: Status = Starting): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      Behaviors.receiveMessage {
        case Request(protocol.command.Activate, replyTo) =>
          context.log.info("[{}]", currentStatus.toString.toUpperCase)
          // https://doc.akka.io/docs/akka/current/typed/dispatchers.html#selecting-a-dispatcher
          val web = context.spawn(Web(), "web", DispatcherSelector.blocking())
          web ! Request(protocol.command.Activate, context.self)
          context.log.info(
            "[{}]-{}->[{}]",
            currentStatus.toString.toUpperCase,
            protocol.command.Activate.toString,
            Ready.toString.toUpperCase)
          replyTo ! Response(currentStatus)
          Behaviors.same
        case Request(protocol.command.Status, replyTo) =>
          replyTo ! Response(currentStatus)
          Behaviors.same
        case Response(responseStatus) =>
          context.log.info("[{}]", responseStatus.toString.toUpperCase)
          responseStatus match {
            case Ready    => ready()
            case Fatal    => fatal()
            case Starting => Behaviors.same
          }
      }
    }

  private def ready(currentStatus: Status = Ready): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      Behaviors.receiveMessage {
        case Request(protocol.command.Status, replyTo) =>
          replyTo ! Response(currentStatus)
          Behaviors.same
        case _ =>
          Behaviors.same
      }
    }

  private def fatal(currentStatus: Status = Fatal): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      Behaviors.receiveMessage {
        case Request(protocol.command.Status, replyTo) =>
          replyTo ! Response(currentStatus)
          Behaviors.same
        case _ =>
          Behaviors.same
      }
    }
}
