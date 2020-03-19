import Profile.{host, port, serviceName}
import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import http.HttpService.{routes, start}

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2020-03-15
  */
object App {
  private val rootBehavior = Behaviors.setup[Nothing] { context =>
    import system.executionContext

    system.log.info("[STARTING]")
    val futureBinding = start(host, port, routes, context.system)

    futureBinding.onComplete {
      case Success(binding) =>
        system.log.info("[READY]")
        Done
      case Failure(ex) =>
        system.log.error("[FATAL]", ex)
        system.terminate()
    }

    Behaviors.empty
  }

  implicit val system: ActorSystem[Nothing] =
    ActorSystem[Nothing](rootBehavior, serviceName)

  def main(args: Array[String]): Unit = {}
}
