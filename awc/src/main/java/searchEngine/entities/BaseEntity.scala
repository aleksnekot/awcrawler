package searchEngine.entities

import org.squeryl.KeyedEntity

class BaseEntity extends KeyedEntity[Int] {
  val id: Int = 0
}
