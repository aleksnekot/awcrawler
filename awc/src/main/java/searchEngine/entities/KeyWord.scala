package searchEngine.entities

import searchEngine.SearchDbSchema

class KeyWord(val word: String) extends BaseEntity {
  lazy val webPages = SearchDbSchema.webPageKeyWords.right(this)
}