package behaviors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import protocol.message.Message

/**
  * @author kasonchan
  * @since 2020-03-22
  */
object System {
  def apply(): Behavior[Message] = init()

  private def init(): Behavior[Message] =
    Behaviors.setup[Message] { context =>
      // Do nothing
      Behaviors.empty
    }
}
