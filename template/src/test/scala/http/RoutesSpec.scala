package http

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
//import app.Service
//import app.Service._
import http.HttpService.routes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
//import protocol.message.Response
//import protocol.status.{Fatal, Ready, Starting}

//import scala.concurrent.Future

/**
 * @author kasonchan
 * @since 2020-03-19
 */
class RoutesSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with ScalatestRouteTest {
  "routes" should {
    "returns STARTING (GET /status)" in {
//      (Future(Response(Starting)))
      HttpRequest(uri = "/status") ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`text/plain(UTF-8)`)
        entityAs[String] should ===("STARTING")
      }
    }

    // TODO: Fix this to let routes to have input parameter in order to fulfill 100% code coverage
//    "returns READY (GET /status)" in {
//      //      (Future(Response(Ready)))
//      HttpRequest(uri = "/status") ~> routes ~> check {
//        status should ===(StatusCodes.OK)
//        contentType should ===(ContentTypes.`text/plain(UTF-8)`)
//        entityAs[String] should ===("READY")
//      }
//    }
//
//    "returns FATAL (GET /status)" in {
//      // (Future(Response(Fatal)))
//      HttpRequest(uri = "/status") ~> routes ~> check {
//        status should ===(StatusCodes.OK)
//        contentType should ===(ContentTypes.`text/plain(UTF-8)`)
//        entityAs[String] should ===("FATAL")
//      }
//    }
  }
}
