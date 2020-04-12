package http

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

import scala.concurrent.Future

/**
  * @author kasonchan
  * @since 2020-03-15
  */
object HttpService {
  val routes: Route = path("status") {
    get {
      complete {
        "READY"
      }
    }
  }

  def start(
      host: String,
      port: Int,
      routes: Route,
      system: ActorSystem[_]
  ): Future[Http.ServerBinding] = {
    // Akka HTTP still needs a classic ActorSystem to start
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext

    Http().bindAndHandle(routes, interface = host, port = port)
  }
}
