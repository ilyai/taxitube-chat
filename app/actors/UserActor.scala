package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.event.LoggingReceive
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import akka.actor.ActorRef
import akka.actor.Props
import scala.xml.Utility


class UserActor(name: String, board: ActorRef, out: ActorRef) extends Actor with ActorLogging {


  override def preStart() = {
    BoardActor() ! Subscribe
  }

  def receive = LoggingReceive {
    case Message(mname, s) if sender == board => {
      val js = Json.obj("type" -> "message", "name" -> mname, "msg" -> s)
      out ! js
    }
    case js: JsValue => (js \ "msg").validate[String] map { Utility.escape(_) }  map { board ! Message(name, _ ) } 
    case other => log.error("unhandled: " + other)
  }
}

object UserActor {
  def props(name: String)(out: ActorRef) = Props(new UserActor(name, BoardActor(), out))
}
