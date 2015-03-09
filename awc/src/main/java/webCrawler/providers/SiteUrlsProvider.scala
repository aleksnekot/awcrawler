package webCrawler.providers

import awc.ConfigSettings
import scala.io.Source

class SiteUrlsProvider extends TSiteUrlsProvider {
  def GetSiteUrls() : List[String] = {
    Source.fromURL(getClass.getResource(ConfigSettings.siteUrlsFile))
      .getLines().map(line => line.trim)
      .toList
  }
}
