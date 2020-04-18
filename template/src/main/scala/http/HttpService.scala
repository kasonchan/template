package http

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import app.{Profile, Service}
import org.slf4j.{Logger, LoggerFactory}
import protocol.message.Response

import scala.concurrent.{Await, Future}
import scala.util.{Success, Try}

/**
  * @author kasonchan
  * @since 2020-03-15
  */
object HttpService {
  private val log: Logger = LoggerFactory.getLogger(super.getClass)

  val routes: Route = path("status") {
    get {
      complete {
        Try {
          Await.result(Service.status, Profile.bindingTimeoutInMilliseconds)
        } match {
          // TODO: Fix this to let routes to have input parameter in order to fulfill 100% code coverage
          case Success(Response(s)) =>
            log.debug("HttpService responses {}", s)
            s"${s.toString.toUpperCase}"
//        case Success(Request(command, replyTo)) =>
//          Service.system.log
//            .error("HttpService responses Request({}, {})", command, replyTo)
//          s"${Fatal.toString.toUpperCase}"
//        case Failure(e) =>
//          Service.system.log.error("HttpService responses {}", e)
//          s"${Fatal.toString.toUpperCase}"
        }
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

    log.debug("Binding to /{}:{}", host, port)
    Http().bindAndHandle(routes, interface = host, port = port)
  }
}
