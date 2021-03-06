package app

import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.duration._

/**
 * @author kasonchan
 * @since 2020-03-15
 */
object Profile {
  implicit val config: Config = ConfigFactory.load()
  val serviceName: String = config.getString("ad.service.name")
  val serviceHost: String = config.getString("ad.service.host")
  val servicePort: Int = config.getInt("ad.service.port")
  val bindingTimeoutInMilliseconds: FiniteDuration =
    config.getInt("ad.service.binding.timeout.in.milliseconds").milliseconds
}
