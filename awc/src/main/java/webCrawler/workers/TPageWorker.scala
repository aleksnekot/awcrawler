package webCrawler.workers

import org.jsoup.nodes.Document

trait TPageWorker {
  def processUrlPage(url: String, document: Document)
}
