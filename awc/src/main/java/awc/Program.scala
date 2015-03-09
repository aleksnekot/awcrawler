package awc

import searchEngine.SearchDbSchema
import webCrawler._
import webCrawler.providers._
import webCrawler.workers._
import webService.BootService

object Program {
  def main(args: Array[String]) {

    if (args.length == 0) {
      return
    }

    val arg = args(0)

    arg match {
      case "--crawler" => SearchDbSchema.init(); startWebCrawl()
      case "--service" => SearchDbSchema.init(); startRestService()
    }
  }

  def startWebCrawl() = {
    val siteUrlsProvider: TSiteUrlsProvider = new SiteUrlsProvider
    val pageWorker: TPageWorker = new PageWorker
    val webCrawler: WebCrawler = new WebCrawler(siteUrlsProvider, pageWorker)

    webCrawler.process()
  }

  def startRestService() = {
    BootService.start()
  }
}
