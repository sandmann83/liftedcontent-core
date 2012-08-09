package eu.sbradl.liftedcontent.core.lib

import eu.sbradl.liftedcontent.core.model.SiteMetaData
import net.liftweb.mapper.By
import net.liftweb.http.S

object SiteMetaDataHelpers {

  def metadata: Seq[SiteMetaData] = metadataFor(S.uri.replaceFirst("/", ""))
  def metadataFor(url: String) = SiteMetaData.findAll(By(SiteMetaData.url, url))
  
}