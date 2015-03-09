package searchEngine.providers

import org.squeryl.PrimitiveTypeMode._
import searchEngine.SearchDbSchema
import searchEngine.entities.WebPage

class WebPagesProvider extends TWebPagesProvider {
  private class WebPageWithCountedWords(val webPage: WebPage, val keyWordsNumber: Int)

  override def addWebPage(name: String, url: String) : WebPage = {
    var result: WebPage = null

    inTransaction {
      result = SearchDbSchema.webPages.insert(new WebPage(name, url))
    }

    result
  }

  def getWebPagesByKeywords(keywords: List[String]) : Map[WebPage, Int] = {
    var webPagesWithCountedWords: List[WebPageWithCountedWords] = null

    inTransaction {
      webPagesWithCountedWords = SearchDbSchema.webPages
        .map(wp => new WebPageWithCountedWords(
          wp,
          wp.keyWords.count(k => keywords.contains(k.word))))
        .toList
    }

    webPagesWithCountedWords = webPagesWithCountedWords
      .sortWith(_.keyWordsNumber > _.keyWordsNumber)

    webPagesWithCountedWords.map { wp => (wp.webPage, wp.keyWordsNumber) } toMap
  }
}
