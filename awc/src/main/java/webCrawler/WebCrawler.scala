package webCrawler

import awc.ConfigSettings
import org.jsoup.nodes.Document
import org.jsoup.Jsoup
import webCrawler.providers._
import webCrawler.workers._
import org.jsoup.nodes.Element
import org.jsoup.Connection

class WebCrawler(siteUrlsProvider: TSiteUrlsProvider, pageWorker: TPageWorker) {

  private var processedPageUrls: List[String] = List[String]()

  def process() {
    siteUrlsProvider.GetSiteUrls().par.map(url => processPage(url))
  }

  private def processPage(siteUrl: String, processUrl: String = null) {

    val url = prepareUrl(if (processUrl != null) processUrl else siteUrl)

    if (checkIfUrlProcessed(url)) {
      return
    }

    addUrlToProcessed(url)

    val document = getDocumentByUrl(url)

    if (document.isEmpty) {
      return
    }

    processUrlPage(url, document.get)

    getLinksOnPageFromUrl(document.get)
      .filter(link => link.attr("abs:href").contains(siteUrl))
      .par.map(link => processPage(siteUrl, link.attr("abs:href")))
  }

  private def prepareUrl(url: String) = {
    val indexOfHash = url.indexOf('#')

    if (indexOfHash >= 0) {
      url.substring(0, indexOfHash)
    } else {
      url
    }
  }

  private def getDocumentByUrl(url: String): Option[Document] =
    try {
      Some(Jsoup.connect(url)
        .header("Content-type", "Content-Type:text/html; charset=UTF-8")
        .method(Connection.Method.GET)
        .timeout(ConfigSettings.requestTimeout)
        .get())
    } catch {
      case e: Exception => e.printStackTrace(); None;
    }

  private def processUrlPage(url: String, document: Document) {
    pageWorker.processUrlPage(url, document)
  }

  private def checkIfUrlProcessed(url: String): Boolean = {
    processedPageUrls.contains(url)
  }

  private def addUrlToProcessed(url: String) {
    processedPageUrls = processedPageUrls.::(url)
  }

  private def getLinksOnPageFromUrl(doc: Document): Array[Element] = {
    if (doc == null) {
      return new Array[Element](0)
    }

    doc.select("a[href]").toArray.map(_.asInstanceOf[Element])
  }
}
