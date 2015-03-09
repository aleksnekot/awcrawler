package searchEngine.providers

import searchEngine.SearchDbSchema
import searchEngine.entities._
import org.squeryl.PrimitiveTypeMode._

class KeyWordsProvider extends TKeyWordsProvider {
  override def addKeyWordsToFile(keywords: List[String], webPageId: Int = -1) : List[KeyWord] = {
    var result: List[KeyWord] = null

    inTransaction {
      val nonExistedKeyWords = keywords
        .filter(w => !(SearchDbSchema.keyWords.map(k => k.word).toList contains w))
        .map(w => new KeyWord(w))
        .toList

      SearchDbSchema.keyWords.insert(nonExistedKeyWords)

      result = SearchDbSchema.keyWords
        .filter(k => keywords.contains(k.word))
        .toList

      if (webPageId > 0) {
        val webPageOption = SearchDbSchema.webPages.find(w => w.id == webPageId)
        if (webPageOption.nonEmpty) {
          val webPage = webPageOption.get

          val nonAssociatedKeyWords = result
            .filter(kw => !(webPage.keyWords.map(k => k.word).toList contains kw.word))

          nonAssociatedKeyWords.foreach(webPage.keyWords.associate)
        }
      }
    }

    result
  }
}
