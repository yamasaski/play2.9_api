package controllers.responses

import play.api.libs.json.*

case class InsertSiteResponse(siteId: Long, siteName: String, count: Long)

object InsertSiteResponse {
  implicit val format: Format[InsertSiteResponse] = Json.format

}