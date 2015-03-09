package searchEngine.text

import searchEngine.providers.{TStopWordsProvider, StopWordsProvider}

class TextParser {
  private val BottomRateBound = 1
  private val TopRateBound = 7

  var stopWordsProvider: TStopWordsProvider = new StopWordsProvider

  private var words: List[WordInfo] = null
  def getWords = words

  private var keyWords: List[String] = null
  def getKeyWords = keyWords

  private var totalCount = 0
  def getTotalCount = totalCount

  private val splitters = Array(',', ':', ';', '?', '.', '/', '!', '"', '-', ' ', '|')

  def init(text: String) {
    words = List[WordInfo]()
    val allWords = split(text)
    totalCount = 0

    allWords.foreach(word => {
      if (word != "") {
        totalCount += 1

        var wordInfo: WordInfo = null
        val wordInfoOption = words.find(w => w.word == word)
        if (wordInfoOption.isEmpty) {
          wordInfo = new WordInfo(word)
          words = words.::(wordInfo)
        } else {
          wordInfo = wordInfoOption.get
        }
        wordInfo.count += 1
      }
    })

    words = words.sortWith(_.count > _.count)
  }

  def split(text: String): Array[String] = {
    text.split(splitters).map(_.toLowerCase)
  }

  def findKeyWords(): List[String] = {
    filterWords()
    setRate()

    keyWords = words
      .filter(w => w.rate >= BottomRateBound && w.rate <= TopRateBound)
      .map(w => w.word)
      .toList

    keyWords
  }

  def filterWords() = {
    val stopWords = stopWordsProvider.getAll
    words = words.filter(w => !stopWords.contains(w.word))
  }

  def setRate() = {
    words = words.sortWith(_.count > _.count)

    var initRate = 0
    var prevCount: Long = -1

    words.foreach(word => {
      if (word.count != prevCount) {
        initRate += 1
        prevCount = word.count
      }
      word.rate = initRate
    })
  }
}
