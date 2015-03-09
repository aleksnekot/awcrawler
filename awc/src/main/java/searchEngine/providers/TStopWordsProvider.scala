package searchEngine.providers

trait TStopWordsProvider {
  def getAll: Set[String]
}
