import Profile.{serviceHost, servicePort, serviceName}
import org.scalatest.{Matchers, WordSpec}

/**
  * @author kasonchan
  * @since 2020-03-19
  */
class ProfileSpec extends WordSpec with Matchers {
  val expectedServiceName = "service"
  val expectedServiceHost = "127.0.0.1"
  val expectedServicePort = 11010

  "ad.service.name" should {
    s"equals to ${expectedServiceName}" in serviceName === expectedServiceName
  }

  "ad.service.host" should {
    s"equals to ${expectedServiceHost}" in serviceHost === expectedServiceHost
  }

  "ad.service.port" should {
    s"equals to ${expectedServicePort}" in servicePort === expectedServicePort
  }
}
