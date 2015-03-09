package webService.rest

import java.text.{ParseException, SimpleDateFormat}
import java.util.Date
import akka.event.slf4j.SLF4JLogging
import net.liftweb.json.Serialization._
import net.liftweb.json.{DateFormat, Formats}
import searchEngine.WebPageSearchProvider
import spray.http._
import spray.httpx.unmarshalling._
import spray.routing._

trait RestService extends HttpService with SLF4JLogging {
  private val webPageSearchProvider = new WebPageSearchProvider

  implicit val executionContext = actorRefFactory.dispatcher

  implicit val liftJsonFormats = new Formats {
    val dateFormat = new DateFormat {
      val sdf = new SimpleDateFormat("yyyy-MM-dd")

      def parse(s: String): Option[Date] = try {
        Some(sdf.parse(s))
      } catch {
        case e: Exception => None
      }

      def format(d: Date): String = sdf.format(d)
    }
  }

  implicit val string2Date = new FromStringDeserializer[Date] {
    def apply(value: String) = {
      val sdf = new SimpleDateFormat("yyyy-MM-dd")
      try Right(sdf.parse(value))
      catch {
        case e: ParseException =>
          Left(MalformedContent("'%s' is not a valid Date value" format value, e))
      }
    }
  }

  implicit val customRejectionHandler = RejectionHandler {
    case rejections => mapHttpResponse {
      response =>
        response.withEntity(HttpEntity(ContentType(MediaTypes.`application/json`),
          write(Map("error" -> response.entity.asString))))
    } {
      RejectionHandler.Default(rejections)
    }
  }

  val rest = respondWithMediaType(MediaTypes.`application/json`) {
    path("search") {
        get {
          parameters('keywords.as[String] ?).as(WebPageSearchParameter) {
            searchParameters: WebPageSearchParameter => {
              ctx: RequestContext =>
                handleRequest(ctx) {
                  log.debug("Searching for web page with parameters: %s".format(searchParameters))
                  webPageSearchProvider.findWebPages(searchParameters.keywords)
                }
            }
          }
        }
    }
  }

  protected def handleRequest(ctx: RequestContext, successCode: StatusCode = StatusCodes.OK)(action: => Option[Any]) {
    action match {
      case Some(result: Object) =>
        ctx.complete(successCode, write(result))
      case _ =>
        ctx.complete(StatusCodes.InternalServerError)
    }
  }
}