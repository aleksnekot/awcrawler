package searchEngine.providers

import awc.ConfigSettings
import scala.io.Source

class StopWordsProvider extends TStopWordsProvider {
  override def getAll: Set[String] = {
    Source.fromURL(getClass.getResource(ConfigSettings.stopWordsFile))
      .getLines().map(line => line.trim)
      .toSet
  }
}
