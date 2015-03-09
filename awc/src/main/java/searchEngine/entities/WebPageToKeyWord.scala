package searchEngine.entities

import org.squeryl.KeyedEntity
import org.squeryl.dsl.CompositeKey2
import org.squeryl.PrimitiveTypeMode._

class WebPageToKeyWord(val webPageId: Int, val keyWordId: Int) extends KeyedEntity[CompositeKey2[Int,Int]] {
  def id = compositeKey(webPageId, keyWordId)
}