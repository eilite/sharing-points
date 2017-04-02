package actors

import akka.actor.{Actor, ActorRef}

import scala.collection.Set

object SharingPointActor {

  case class GetAvailableBycicles()
  case class SharingPointInternalState( totalBycicles: Int, availableBycicles: Int)
  def apply(numberOfBycicle: Int) = new SharingPointActor(numberOfBycicle)
}

class SharingPointActor(numberOfBycicle: Int) extends Actor {
  import SharingPointActor._
  val availableBycicles: Int = numberOfBycicle
  def receive = {
    case GetAvailableBycicles() =>
      sender ! new SharingPointInternalState(numberOfBycicle, availableBycicles)
  }
}
