package searchEngine.text

class WordInfo(val word: String) {
  var rate: Integer = 0
  var count: Long = 0

  def getFrequency(totalCount: Integer): Double = {
    count / totalCount
  }
}
