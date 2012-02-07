package de.sbradl.liftedcontent.core.snippet

import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds
import net.liftweb.mapper.By
import de.sbradl.liftedcontent.core.model.ACLEntry
import net.liftweb.http.SHtml.BasicElemAttr
import de.sbradl.liftedcontent.core.model.Role
import scala.xml.Text
import net.liftweb.sitemap.SiteMap
import de.sbradl.liftedcontent.pages.model.{ Page => PageModel }

class Permissions {

  private def deny(url: String, roleId: Long): JsCmd = {
    val entryBox = ACLEntry.find(By(ACLEntry.url, url), By(ACLEntry.role, roleId))

    entryBox match {
      case Full(entry) => entry.delete_!
      case _ =>
    }

    JsCmds.Noop
  }

  private def allow(url: String, roleId: Long): JsCmd = {
    if (ACLEntry.count(By(ACLEntry.url, url), By(ACLEntry.role, roleId)) == 0) {
      val entry = ACLEntry.create
      entry.url(url)
      entry.role(roleId)
      entry.save
    }

    JsCmds.Noop
  }

  private def onPermissionChanged(url: String, roleId: Long, value: Boolean): JsCmd = value match {
    case true => allow(url.replaceFirst("default/", ""), roleId)
    case false => deny(url.replaceFirst("default/", ""), roleId)
  }

  def render = {
    val roles = Role.findAll

    val rawUrls = LiftRules.siteMap.openOr(SiteMap()).menus map (menu => menu.loc.link.uriList.mkString("/"))
    val urls: List[String] = (rawUrls map (url => url.contains("/") match {
      case true => url
      case false => "default/" + url
    })) ++ (PageModel.findAll map (p => p.url))

    val groupedUrls = (urls.sorted groupBy (
      url => {
        url.split("/", 2).head
      })) - "error" - "setup"

    val urlKeys = groupedUrls.keys.toList.sorted
    
    val alwaysAllowed = List("user_mgt/login", "user_mgt/logout", "user_mgt/validate_user")

    "* *" #> <table>
               <thead>
                 <tr>
                   <td></td>
                   { roles map (role => <td>{ role.name }</td>) }
                 </tr>
               </thead>
               {
                 urlKeys map {
                   key =>
                     <tr>
                       <td><b>{ key }</b></td>
                       { roles map (r => <td></td>) }
                     </tr> ++
                       {
                         groupedUrls(key) map {
                           url =>
                             <tr>
                               <td>{ url }</td>
                               {
                                 roles map (
                                   role => {
                                     val isAdminRole = role.id.is == Role.adminRoleID

                                     if (isAdminRole || alwaysAllowed.contains(url)) {
                                       <td><img src="/images/allowed.png" alt="ALLOWED"/></td>
                                     } else {
                                       val isAllowed = ACLEntry.count(By(ACLEntry.url, url), By(ACLEntry.role, role.id.is)) == 1 || isAdminRole
                                       <td>{ SHtml.ajaxCheckbox(isAllowed, (value) => onPermissionChanged(url, role.id.is, value)) }</td>
                                     }
                                   })
                               }
                             </tr>
                         }
                       }
                 }
               }
             </table>
  }
}