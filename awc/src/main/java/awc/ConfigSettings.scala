package awc

import com.typesafe.config.ConfigFactory
import util.Try

object ConfigSettings {
  val config = ConfigFactory.load()

  lazy val requestTimeout =
    Try(config.getInt("webcrawler.requestTimeout")).getOrElse(10 * 1000)
  lazy val databaseProperties =
    Try(config.getString("webcrawler.databaseProperties")).getOrElse("/database.properties")
  lazy val stopWordsFile =
    Try(config.getString("webcrawler.stopWordsFile")).getOrElse("/stopwords.txt")
  lazy val siteUrlsFile =
    Try(config.getString("webcrawler.siteUrlsFile")).getOrElse("/siteurls.txt")
}
