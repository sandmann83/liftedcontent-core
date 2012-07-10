package eu.sbradl.liftedcontent.pages.model

import eu.sbradl.liftedcontent.core.model.User
import net.liftweb.mapper.OneToMany
import net.liftweb.mapper.CreatedUpdated
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedLocale
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.MappedText
import net.liftweb.mapper.MappedBoolean
import net.liftweb.http.S
import net.liftweb.util.Helpers._

object PageContent extends PageContent with LongKeyedMetaMapper[PageContent]

class PageContent extends LongKeyedMapper[PageContent] with IdPK with CreatedUpdated
  with OneToMany[Long, PageContent] {

  def getSingleton = PageContent

  object page extends MappedLongForeignKey(this, Page)
  object translator extends MappedLongForeignKey(this, User)

  object language extends MappedLocale(this) {
    override def displayName = S ? "LANGUAGE"
  }

  object title extends MappedString(this, 128) {
    override def displayName = S ? "TITLE"
  }
  
  def url = "/page/" + encodedTitle
  def encodedTitle = urlEncode(title.is)
  
  object text extends MappedText(this)

  object published extends MappedBoolean(this) {
    override def displayName = S ? "PUBLISH_IMMEDIATELY"
  }

}