package de.sbradl.liftedcontent.pages.model

import de.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.mapper.OneToMany
import net.liftweb.mapper.By
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.util.Helpers.urlEncode
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedString
import de.sbradl.liftedcontent.core.lib.ACL

object Page extends Page with LongKeyedMetaMapper[Page] {

  def findAllByCurrentLocale = {
    val contents = PageContent.findAll(By(PageContent.language, S.locale.getLanguage))
    
    contents filter {
      content => {
        val page = content.page.obj.open_!
        
        ACL.isAllowed(page.url)
      }
    }
  }

}

class Page extends LongKeyedMapper[Page] with IdPK with OneToMany[Long, Page] {

  def getSingleton = Page

  object name extends MappedString(this, 32) {
    override def displayName = S ? "NAME"
  }
  
  def url = "page/" + encodedName
  def encodedName = urlEncode(name)

  object author extends MappedLongForeignKey(this, User)
  object contents extends MappedOneToMany(PageContent, PageContent.page) with Cascade[PageContent]
  
}