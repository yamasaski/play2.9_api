package controllers.forms

import play.api.data.Forms._
import play.api.data.Form

object SiteForm {

  case class SiteForm(siteId: Long, siteName: String)
  object SiteForm {
    def unapply(req: SiteForm): Option[(Long, String)] = {
      Some((req.siteId, req.siteName))
    }
  }

  val form = Form(
    mapping(
      "siteId" -> longNumber,
      "siteName" -> nonEmptyText
    )(SiteForm.apply)(SiteForm.unapply)
  )
}