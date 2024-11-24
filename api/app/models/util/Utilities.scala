package util

import play.api.libs.json._

def toMessage(m: String) = Json.obj("message" -> m)
