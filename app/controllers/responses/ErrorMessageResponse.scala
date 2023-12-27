package controllers.responses

import play.api.data.FormError
import play.api.libs.json.*


case class ErrorMessageResponse(message: Seq[String])

object ErrorMessageResponse {
  implicit val format: Format[ErrorMessageResponse] = Json.format

  def formError(formErrors: Seq[FormError]): ErrorMessageResponse =
    val errorList = formErrors.map(e => e.key + "が不正です。（" + e.message + "）")
    new ErrorMessageResponse(errorList)

}

