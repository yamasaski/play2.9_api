package controllers

import play.api.mvc.*
import javax.inject.*

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def index = Action {
    Ok("ok")
  }

}

