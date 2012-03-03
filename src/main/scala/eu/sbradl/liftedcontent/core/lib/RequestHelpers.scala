package eu.sbradl.liftedcontent.core.lib

import net.liftweb.http.Req
import net.liftweb.http.S
import net.liftweb.http.LiftRules
import net.liftweb.sitemap.SiteMap
import eu.sbradl.liftedcontent.pages.model.{ Page => PageModel }

object RequestHelpers {

  def path(r: Req) = r.path.wholePath.mkString("/")
  def currentPath = S.request.map(r => path(r))
  
  def allPaths: List[String] = allPaths()
  def allPaths(prefixForUrlWithoutSlash: String = "") = {
    val rawUrls = LiftRules.siteMap.openOr(SiteMap()).menus map (menu => menu.loc.link.uriList.mkString("/"))
    
    val urls: List[String] = (rawUrls map (url => url.contains("/") match {
      case true => url
      case false => prefixForUrlWithoutSlash + url
    })) ++ (PageModel.findAll map (p => p.url))
    
    urls sorted
  }
}