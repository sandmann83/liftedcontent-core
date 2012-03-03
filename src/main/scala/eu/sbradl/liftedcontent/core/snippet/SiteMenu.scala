package eu.sbradl.liftedcontent.core.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.sitemap.SiteMap
import net.liftweb.sitemap.MenuItem
import scala.xml.NodeSeq
import scala.xml.Text
import net.liftweb.common.Full

class SiteMenu {
  val PrimaryMenuGroup = "primary"
  val SecondaryMenuGroup = "secondary"
  val UserMenuGroup = "user"

  private def markup(menuItem: MenuItem): NodeSeq = {
    var currentLocation = ("/" + S.request.open_!.path.wholePath.mkString("/"))
    
    currentLocation = currentLocation endsWith "/index" match {
      case true => currentLocation.replace("/index", "/")
      case false => currentLocation
    }

    val isCurrent = currentLocation == menuItem.uri.text
    val isParent = currentLocation startsWith menuItem.uri.text

    val submenus = !menuItem.kids.isEmpty && isParent match {
      case true => <ul>{ menuItem.kids map (markup(_)) }</ul>
      case false =>
    }

    val currentMenu = isCurrent match {
      case true => <span>{ menuItem.text }</span>
      case false => <a href={ menuItem.uri }>{ menuItem.text }</a>
    }

    <li>
      { currentMenu }
      { submenus }
    </li>
  }

  def render = {
    val sitemap = LiftRules.siteMap.openOr(SiteMap())
    
    val groupsAttr = S.attr("groups").openOr("")
    
    var groups: List[String] = groupsAttr.split(",").toList
    
    S.attr("group") match {
      case Full(group) => groups ::= group
      case _ =>
    }
    
    val menus = groups flatMap {
    	 menu => sitemap.menuForGroup(menu).lines
    }

    "* *" #> <ul data-role="listview">{ menus map (markup(_)) }</ul>
  }

}