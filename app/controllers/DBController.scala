package controllers

import play.api.mvc.*
import play.api.libs.json.Json
import play.api.data.Form
import javax.inject.*
import play.api.Logger

import services.*
import responses.*

@Singleton
class DBController @Inject()(cc: ControllerComponents, service: DBService) extends AbstractController(cc){

  private val logger = Logger(this.getClass)

  def getSite = Action { implicit request: Request[AnyContent] =>
    val res = service.getSite
    Ok(Json.toJson(res))
  }

  def insertSite = Action { implicit request: Request[AnyContent] =>
    import controllers.forms.SiteForm.*

    def failure(badForm: Form[SiteForm]) = {
      BadRequest(Json.toJson(ErrorMessageResponse.formError(badForm.errors)))
    }
    def success(form: SiteForm) = {
      val res = service.insertSite(form)
      res match
        case Right(res) => Ok(Json.toJson(res))
        case Left(error) => BadRequest(Json.toJson(error))
    }
    form.bindFromRequest().fold(failure, success)
  }

  def updateSite = Action { implicit request: Request[AnyContent] =>
    import controllers.forms.SiteForm.*

    def failure(badForm: Form[SiteForm]) = {
      BadRequest(Json.toJson(ErrorMessageResponse.formError(badForm.errors)))
    }

    def success(form: SiteForm) = {
      val res = service.updateSite(form)
      res match
        case Right(res) => Ok(Json.toJson(res))
        case Left(error) => BadRequest(Json.toJson(error))
    }
    form.bindFromRequest().fold(failure, success)
  }

  def deleteSite = Action { implicit request: Request[AnyContent] =>
    import controllers.forms.SiteIdForm.*

    def failure(badForm: Form[SiteIdForm]) = {
      BadRequest(Json.toJson(ErrorMessageResponse.formError(badForm.errors)))
    }

    def success(form: SiteIdForm) = {
      val res = service.deleteSite(form.siteId)
      res match
        case Right(res) => Ok(Json.toJson(res))
        case Left(error) => BadRequest(Json.toJson(error))
    }

    form.bindFromRequest().fold(failure, success)
  }

  def testTransaction = Action { implicit request: Request[AnyContent] =>
    val result = service.testTransaction
    Ok(result.toString)
  }

}

