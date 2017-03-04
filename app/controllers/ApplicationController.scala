package controllers

import play.api.mvc.{Action, Controller}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.duration._
import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import javax.inject._

import actors.MainActor
import actors.MainActor.SayHello

@Singleton
class AppController  @Inject() (system: ActorSystem) extends Controller {
  val orchestratorActor = system.actorOf(MainActor.props, "main-actor")

  def index = Action.async {
    (orchestratorActor ? SayHello(" YOU "))(Timeout(1000 millis)).mapTo[String].map { message =>
      Ok(message)
    }
  }
}
