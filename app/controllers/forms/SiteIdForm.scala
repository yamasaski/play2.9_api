package controllers.forms

import play.api.data.Form
import play.api.data.Forms.*

object SiteIdForm {

  case class SiteIdForm(siteId: Long)
  object SiteIdForm {
    def unapply(req: SiteIdForm): Option[(Long)] = {
      Some((req.siteId))
    }
  }

  val form = Form(
    mapping(
      "siteId" -> longNumber
    )(SiteIdForm.apply)(SiteIdForm.unapply)
  )
}