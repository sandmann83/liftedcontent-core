package de.sbradl.liftedcontent.core.model

import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.http.S
import net.liftweb.mapper.MappedString
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.CRUDify
import net.liftweb.sitemap.SiteMap
import net.liftweb.common.Full
import net.liftweb.http.LiftRules

object StartPage extends StartPage with LongKeyedMetaMapper[StartPage] {

  def insert(url: String) {
    if (find().isEmpty) {
      val startPage = StartPage.create
      startPage.url(url)
      startPage.save
    }

  }

}

class StartPage extends LongKeyedMapper[StartPage] with IdPK {

  def getSingleton = StartPage

  object url extends MappedString(this, 128) {
    override def displayName = S ? "START_PAGE"
  }
}
