package models

import java.util.Date

case class SiteMst(siteId: Long, siteName: String, registDate: Date = new Date())
