package webCrawler.providers

trait TSiteUrlsProvider {
  def GetSiteUrls() : List[String]
}
