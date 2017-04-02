package controllers

import play.api.mvc.{Action, Controller}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext


class AppController extends Controller {
  def index = Action.async {
    Future { Ok("OK!") }
  }
}
