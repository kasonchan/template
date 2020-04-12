package app

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since 2020-03-19
  */
class ProfileSpec extends AnyWordSpec with Matchers {
  private val expectedServiceName = "service"
  private val expectedServiceHost = "127.0.0.1"
  private val expectedServicePort = 11010
  private val expectedBindingTimeoutInMilliseconds = 30000.milliseconds

  "ad.service.name" should {
    s"equals to ${expectedServiceName}" in Profile.serviceName === expectedServiceName
  }

  "ad.service.host" should {
    s"equals to ${expectedServiceHost}" in Profile.serviceHost === expectedServiceHost
  }

  "ad.service.port" should {
    s"equals to ${expectedServicePort}" in Profile.servicePort === expectedServicePort
  }

  "ad.service.binding.timeout.in.seconds" should {
    s"equals to ${expectedBindingTimeoutInMilliseconds}" in Profile.bindingTimeoutInMilliseconds === expectedBindingTimeoutInMilliseconds
  }
}
