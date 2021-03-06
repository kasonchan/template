package http

import java.net.InetSocketAddress

import akka.stream.BindFailedException
import app.{ Profile, Service }
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Await

/**
 * @author kasonchan
 * @since 2020-03-28
 */
class HttpServiceSpec extends AnyWordSpec with Matchers {
  private val timeoutInMilliseconds = Profile.bindingTimeoutInMilliseconds

  "Http service" must {
    "starts successfully when binding to free 127.0.0.1:9000" in {
      val expectedHost = "127.0.0.1"
      val expectedPort = 9000
      val expectedRoutes = HttpService.routes
      val expectedSystem = Service.system
      val expectedInetSocketAddress =
        new InetSocketAddress(expectedHost, expectedPort)

      Await
        .result(
          HttpService
            .start(expectedHost, expectedPort, expectedRoutes, expectedSystem),
          timeoutInMilliseconds)
        .localAddress mustBe expectedInetSocketAddress
    }

    "starts fail when binding to 127.0.0.1:9000 is not free" in {
      val expectedHost = "127.0.0.1"
      val expectedPort = 9000
      val expectedRoutes = HttpService.routes
      val expectedSystem = Service.system

      intercept[BindFailedException] {
        HttpService.start(
          expectedHost,
          expectedPort,
          expectedRoutes,
          expectedSystem)
        Await.result(
          HttpService
            .start(expectedHost, expectedPort, expectedRoutes, expectedSystem),
          timeoutInMilliseconds)
      }
    }
  }
}
