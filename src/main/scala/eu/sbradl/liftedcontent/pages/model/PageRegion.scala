package eu.sbradl.liftedcontent.pages.model

import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.MappedLongForeignKey
import net.liftweb.mapper.MappedText
import net.liftweb.mapper.MappedString

object PageRegion extends PageRegion with LongKeyedMetaMapper[PageRegion]

class PageRegion extends LongKeyedMapper[PageRegion] with IdPK {

  def getSingleton = PageRegion
  
  object belongsTo extends MappedLongForeignKey(this, PageContent)
  
  object name extends MappedString(this, 16)
  
  object text extends MappedText(this)
  
}