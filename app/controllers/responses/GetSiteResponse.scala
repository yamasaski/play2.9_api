package controllers.responses

import play.api.libs.json.*

import java.util.Date

case class GetSiteResponse(siteId: Long, siteName: String, registDate: String)

object GetSiteResponse {
  implicit val format: Format[GetSiteResponse] = Json.format
}