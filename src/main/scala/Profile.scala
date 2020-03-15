import http.HttpService.start
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.{Config, ConfigFactory}
import http.HttpService.routes

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2020-03-15
  */
object Profile {
  private val rootBehavior = Behaviors.setup[Nothing] { context =>
    import system.executionContext

    val futureBinding = start(host, port, routes, context.system)

    futureBinding.onComplete {
      case Success(binding) =>
        system.log.info("[READY]")
      case Failure(ex) =>
        system.log.error("[FATAL]", ex)
        system.terminate()
    }

    Behaviors.empty
  }

  implicit val config: Config = ConfigFactory.load()
  val serviceName: String = "service"
  val host: String = "127.0.0.1"
  val port: Int = 11010

  implicit val system: ActorSystem[Nothing] =
    ActorSystem[Nothing](rootBehavior, serviceName)
}
