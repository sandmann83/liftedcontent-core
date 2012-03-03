package eu.sbradl.liftedcontent.core.lib

import eu.sbradl.liftedcontent.core.model.SiteMetaData
import net.liftweb.mapper.By

object SiteMetaDataHelpers {

  def metadata: Seq[SiteMetaData] = metadataFor(RequestHelpers.currentPath.openOr("/"))
  def metadataFor(url: String) = SiteMetaData.findAll(By(SiteMetaData.url, url))
  
}