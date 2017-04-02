package actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Future

import actors.SharingPointActor.{GetAvailableBycicles, SharingPointInternalState}

import scala.util.{Failure, Success}


object MainActor {
  def props = Props[MainActor]

  case class SayHello(name: String)
  case class SharingPoint(x: Double, y: Double, totalBycicles: Int, availableBycicles: Int = 0)
  case class GetSharingPoints()
}

class MainActor extends Actor {
  import MainActor._

  implicit val timeout = Timeout(1000 millis)
  //actor refs by coordinates
  val actors: Map[(Double, Double), ActorRef] = Map.empty

  def receive = {
    //create sharing point
    case SharingPoint(x, y, numberOfBycicle, _) =>
      actors += ((x , y) -> context.actorOf(Props(SharingPointActor(numberOfBycicle)), s"actor$x,$y" ))
    //get list of sharing points
    case GetSharingPoints() =>
      if (actors.isEmpty) {sender ! Set.empty}
      val zender = sender
      val actorsResponses = actors.mapValues(actor => (actor ? new GetAvailableBycicles()).mapTo[SharingPointInternalState])
      Future.sequence(actorsResponses.map(entry => entry._2.map(i => (entry._1, i)))).map(_.toMap)
      .onComplete {
        case Success(map) => {
          zender ! map.map(e => new SharingPoint(e._1._1, e._1._2, e._2.totalBycicles, e._2.availableBycicles)).toSet
        }
        case Failure(e) => println(e.getMessage)
      }
  }
}
