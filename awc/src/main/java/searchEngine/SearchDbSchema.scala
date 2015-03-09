package searchEngine

import java.util.Properties
import awc.ConfigSettings
import org.squeryl.adapters.OracleAdapter
import org.squeryl.{Session, Schema}
import searchEngine.entities._
import org.squeryl.PrimitiveTypeMode._

object SearchDbSchema extends Schema {

  val webPages = table[WebPage]
  val keyWords = table[KeyWord]

  val webPageKeyWords =
    manyToManyRelation(webPages, keyWords).
      via[WebPageToKeyWord]((w,k,wk) => (wk.keyWordId === k.id, w.id === wk.webPageId))

  on(webPages)(w => declare(
    w.id is autoIncremented("ID_SEQ")
  ))
  on(keyWords)(k => declare(
    k.id is autoIncremented("ID_SEQ")
  ))

  def init(updateSchemaByConfig: Boolean = false) = {
    import java.util.Locale
    Locale.setDefault(Locale.ENGLISH)

    val properties = new Properties
    properties.load(getClass.getResourceAsStream(ConfigSettings.databaseProperties))

    val driverClassName = properties.getProperty("driverClassName")
    val url = properties.getProperty("url")
    val username = properties.getProperty("username")
    val password = properties.getProperty("password")

    import org.squeryl.SessionFactory
    Class.forName(driverClassName)

    SessionFactory.concreteFactory = Some(()=>
      Session.create(
        java.sql.DriverManager.getConnection(url, username, password),
        new OracleAdapter))

    if (updateSchemaByConfig) {
      val behavior = properties.getProperty("behavior")
      behavior match {
        case "create" => inTransaction {
          create
        }
        case "reset" => inTransaction {
          webPages.deleteWhere(_ => 1 === 1)
          keyWords.deleteWhere(_ => 1 === 1)
        }
      }
    }
  }
}
