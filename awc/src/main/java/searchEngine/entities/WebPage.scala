package searchEngine.entities

import searchEngine.SearchDbSchema

class WebPage(val name: String, val url: String) extends BaseEntity {
  lazy val keyWords = SearchDbSchema.webPageKeyWords.left(this)
}