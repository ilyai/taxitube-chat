package controllers

import scala.Left
import scala.Right
import scala.concurrent.Future

import actors.UserActor
import play.api.Logger
import play.api.Play.current
import play.api.libs.json.JsValue
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.WebSocket

object Application extends Controller {
  val UID = "uid"
  val NAME = "name"
  var counter = 0;

  def index = Action { implicit request =>
    {
      val uid = request.session.get(UID).getOrElse {
        counter += 1
        counter.toString
      }
      val name = request.session.get(NAME).getOrElse("Guest" + uid);
      Ok(views.html.index(name)).withSession {
        Logger.debug(request.session.toString())
        request.session + (UID -> uid) + (NAME -> name)
      }
    }
  }

  def ws = WebSocket.tryAcceptWithActor[JsValue, JsValue] { implicit request =>
    Future.successful(request.session.get(NAME) match {
      case None => Left(Forbidden)
      case Some(name) => Right(UserActor.props(name))
    })
  }
  
  def changeName = Action { implicit request =>
    Redirect(routes.Application.index()).withSession(
        request.session + (NAME -> request.body.asFormUrlEncoded.get(NAME)(0)))
  }

}