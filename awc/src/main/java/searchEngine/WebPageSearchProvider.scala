package searchEngine

import searchEngine.entities.WebPage
import searchEngine.providers.{WebPagesProvider, TWebPagesProvider}

class WebPageSearchProvider {
  private val webPagesProvider: TWebPagesProvider = new WebPagesProvider

  private val splitters = Array(',', ':', ';', '?', '.', '/', '!', '"', '-', ' ')

  def findWebPages(keywords: Option[String]): Option[Map[WebPage, Int]] = {
    keywords match {
      case Some(keywordQuery) =>
        val keywordsArray = keywordQuery.split(splitters).toList
        Option(webPagesProvider.getWebPagesByKeywords(keywordsArray))
      case None => None
    }
  }
}
