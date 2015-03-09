package searchEngine.providers

import searchEngine.entities.KeyWord

trait TKeyWordsProvider {
  def addKeyWordsToFile(keywords: List[String], webPageId: Int = -1) : List[KeyWord]
}
