package actors

import akka.actor._

object MainActor {
  def props = Props[MainActor]

  case class SayHello(name: String)
}

class MainActor extends Actor {
  import MainActor._

  def receive = {
    case SayHello(name: String) =>
      sender() ! "Hello, " + name
  }
}
