package de.sbradl.liftedcontent.core.model
import net.liftweb.mapper._
import net.liftweb.http.S
import net.liftweb.common.Box
import java.util.Locale
import net.liftweb.common.Full
import net.liftweb.common.Empty

object SetupInformation extends SetupInformation with LongKeyedMetaMapper[SetupInformation] {
  override def fieldOrder = List(title, subtitle)

  def findOrDefault = find().openOr(create
    .title("LiftedContent")
    .subtitle("Scala/Lift based CMS"))

  def locale: Box[Locale] = find() match {
    case Full(information) => information.language.is match {
      case null => Empty
      case lang => Full(Locale.forLanguageTag(lang))
    }
    case _ => Empty
  }
}

class SetupInformation extends LongKeyedMapper[SetupInformation] with IdPK {

  def getSingleton = SetupInformation

  object title extends MappedString(this, 64) {
    override def displayName = S ? "WEBSITE_TITLE"
    override def validations = List(
      valMinLen(3, S ? "NAME_TOO_SHORT") _,
      valMaxLen(64, S ? "NAME_TOO_LONG") _)
  }

  object subtitle extends MappedString(this, 128) {
    override def displayName = S ? "WEBSITE_SUBTITLE"
    override def validations = List(
      valMaxLen(128, S ? "NAME_TOO_LONG") _)
  }

  object language extends MappedString(this, 2) {
    override def displayName = S ? "WEBSITE_LANGUAGE"
  }
}