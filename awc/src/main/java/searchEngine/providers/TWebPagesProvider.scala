package searchEngine.providers

import searchEngine.entities.WebPage

trait TWebPagesProvider {
  def addWebPage(name: String, url: String) : WebPage
  def getWebPagesByKeywords(keywords: List[String]) : Map[WebPage, Int]
}
