package webService

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http
import webService.rest.RestServiceActor

object BootService {
  def start() = {
    implicit val system = ActorSystem("rest-service-example")

    val restService = system.actorOf(Props[RestServiceActor], "rest-endpoint")

    IO(Http) ! Http.Bind(restService, Configuration.serviceHost, Configuration.servicePort)
  }
}
