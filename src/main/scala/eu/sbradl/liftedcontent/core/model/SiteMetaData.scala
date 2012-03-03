package eu.sbradl.liftedcontent.core.model

import net.liftweb.mapper._
import net.liftweb.http.S

object SiteMetaData extends SiteMetaData with LongKeyedMetaMapper[SiteMetaData]

class SiteMetaData extends LongKeyedMapper[SiteMetaData] with IdPK {

  def getSingleton = SiteMetaData

  object url extends MappedString(this, 128) {
    override def displayName = S ? "URL"
  }

  object name extends MappedString(this, 16)

  object content extends MappedText(this) {
    override def displayName = S ? "CONTENT"
  }

}