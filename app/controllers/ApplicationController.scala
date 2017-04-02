package controllers

import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.duration._
import scala.collection.Set
import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import javax.inject.{Inject, Singleton}

import actors.MainActor
import actors.MainActor.{SayHello, SharingPoint, GetSharingPoints}

@Singleton
class AppController  @Inject() (system: ActorSystem) extends Controller {
  implicit val timeout = Timeout(1000 millis)
  implicit val sharingPointFormat = Json.format[SharingPoint]

  val orchestratorActor = system.actorOf(MainActor.props, "main-actor")

  def createSharingPoint = Action(parse.json[SharingPoint]) { implicit request =>
      orchestratorActor ! request.body
      Ok
  }

  def getSharingPoints = Action.async {
    (orchestratorActor ? new GetSharingPoints()).mapTo[Set[SharingPoint]].map(sharingPoints => Ok(Json.toJson(sharingPoints)))
  }
}
