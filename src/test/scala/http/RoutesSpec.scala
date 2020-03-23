package http

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import http.HttpService.routes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

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
    "returns READY (GET /users)" in {
      HttpRequest(uri = "/") ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`text/plain(UTF-8)`)
        entityAs[String] should ===("READY")
      }
    }
  }
}