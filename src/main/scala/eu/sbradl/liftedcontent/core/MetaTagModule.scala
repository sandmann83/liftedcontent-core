package eu.sbradl.liftedcontent.core

import eu.sbradl.liftedcontent.util.Module
import eu.sbradl.liftedcontent.core.model.SiteMetaData
import eu.sbradl.liftedcontent.admin.model.BackendArea

class MetaTagModule extends Module {

  override def mappers = List(SiteMetaData)
  
  override def init {
    BackendArea.insert("METATAG_SETTINGS", "metatags.png", "metatags", "METATAGS_SETTINGS_DESCRIPTION")
  }
  
}