package webCrawler.workers

import org.jsoup.nodes.Document
import searchEngine.providers._
import searchEngine.text.TextParser

class PageWorker extends TPageWorker {
  private val webPagesProvider: TWebPagesProvider = new WebPagesProvider
  private val keyWordsProvider: TKeyWordsProvider = new KeyWordsProvider

  def processUrlPage(url: String, document: Document) {
    val content = getContent(document)
    val textParser = new TextParser
    textParser.init(content)
    val keywords = textParser.findKeyWords()
    val webPage = webPagesProvider.addWebPage(getPageTitle(document), url)

    keyWordsProvider.addKeyWordsToFile(keywords, webPage.id)
  }

  def getPageTitle(document: Document): String = {
    val metaOgTitle = document.select("meta[property=og:title]")
    if (!metaOgTitle.isEmpty) {
      metaOgTitle.attr("content")
    } else {
      document.title()
    }
  }

  def getContent(document: Document): String = {

    val hTags = document.select("h1, h2, h3, h4, h5, h6")
    val h1Tags = (hTags.select("h1").text() + "|") * 6
    val h2Tags = (hTags.select("h2").text() + "|") * 5
    val h3Tags = (hTags.select("h3").text() + "|") * 4
    val h4Tags = (hTags.select("h4").text() + "|") * 3
    val h5Tags = (hTags.select("h5").text() + "|") * 2
    val h6Tags = (hTags.select("h6").text() + "|") * 1

    val metaOgDescription = document.select("meta[property=og:description]")
    val description = if (!metaOgDescription.isEmpty) {
      metaOgDescription.attr("content")
    } else {
      ""
    }

    document.body().text() + h1Tags + h2Tags + h3Tags + h4Tags + h5Tags + h6Tags + description
  }
}
