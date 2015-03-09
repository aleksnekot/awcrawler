package webService

import com.typesafe.config.ConfigFactory
import util.Try

object Configuration {
  val config = ConfigFactory.load()
  lazy val serviceHost = Try(config.getString("service.host")).getOrElse("localhost")
  lazy val servicePort = Try(config.getInt("service.port")).getOrElse(8080)
}
