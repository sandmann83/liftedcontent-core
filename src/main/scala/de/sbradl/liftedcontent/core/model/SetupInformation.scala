package de.sbradl.liftedcontent.core.model
import net.liftweb.mapper._
import net.liftweb.http.S

object SetupInformation extends SetupInformation with LongKeyedMetaMapper[SetupInformation] {
  override def fieldOrder = List(name, subtitle, description)
}

class SetupInformation extends LongKeyedMapper[SetupInformation] with IdPK {

  def getSingleton = SetupInformation

  object name extends MappedString(this, 64) {
    override def displayName = S ? "WEBSITE_TITLE"
    override def validations = List(
      valMinLen(3, S ?? "NAME_TOO_SHORT") _,
      valMaxLen(64, S ?? "NAME_TOO_LONG") _)
  }

  object subtitle extends MappedString(this, 128) {
    override def displayName = S ? "WEBSITE_SUBTITLE"
    override def validations = List(
      valMaxLen(128, S ?? "NAME_TOO_LONG") _)
  }

  object description extends MappedTextarea(this, 512) {
    override def displayName = S ? "WEBSITE_DESCRIPTION"
  }
}