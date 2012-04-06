package eu.sbradl.liftedcontent.core.snippet

import net.liftweb.http.S
import net.liftweb.util.Helpers._
import eu.sbradl.liftedcontent.core.lib.SiteMetaDataHelpers
import eu.sbradl.liftedcontent.core.lib.RequestHelpers

class MetaTags {

  def render = {
    "*" #> RequestHelpers.currentPath
//    SiteMetaDataHelpers.metadata.map {
//      metadata => <meta name={metadata.name.is} content={metadata.content.is} />
//    }
  }
  
}