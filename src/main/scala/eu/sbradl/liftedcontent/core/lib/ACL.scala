package eu.sbradl.liftedcontent.core.lib

import eu.sbradl.liftedcontent.core.model.ACLEntry
import eu.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Box
import net.liftweb.common.Full
import net.liftweb.http.Req
import net.liftweb.http.S
import net.liftweb.mapper.By
import net.liftweb.sitemap.Loc.If
import net.liftweb.sitemap.Loc.strToFailMsg
import net.liftweb.sitemap.Loc.IfValue
import eu.sbradl.liftedcontent.core.model.Role
import net.liftweb.util.Helpers._
import eu.sbradl.liftedcontent.pages.model.PageContent
import net.liftweb.common.Logger
import net.liftweb.util.Props

object ACL extends Logger {

  def isAllowed(r: Box[Req]): Boolean = r match {
    case Full(Req("user_mgt" :: "login" :: _, _, _)) => true
    case Full(Req("user_mgt" :: "logout" :: _, _, _)) => true
    case Full(Req("user_mgt" :: "validate_user" :: _, _, _)) => true
    case Full(Req("user_mgt" :: "lost_password" :: _, _, _)) => true
    case Full(Req("user_mgt" :: "reset_password" :: _, _, _)) => true
    case Full(Req("setup" :: _, _, _)) => true
    case Full(Req("error" :: _, _, _)) => true
    case Full(request) => RequestHelpers.path(request) match {
      case "404.html" => true
      case location => isAllowed(location)
    }
    case _ => false
  }

  def locParam(loc: String) = If(() => isAllowed(loc), S ? "MISSING_REQUIRED_PERMISSIONS")

  def isAllowed(originalLocation: String): Boolean = {

    User.currentUser match {
      case Full(user) => if (user.superUser.is) return true
      case _ =>
    }

    val location = originalLocation match {
      case "page" => {
        val pageTitle = urlDecode(S.uri).substring(6)

        PageContent.find(By(PageContent.title, pageTitle)).open_!.page.obj.open_!.url
      }
      case _ => originalLocation
    }

    val effectiveRoles = User.effectiveRoles

    effectiveRoles foreach {
      role => if (ACLEntry.count(By(ACLEntry.url, location), By(ACLEntry.role, role.id.is)) > 0) {
        logAccess(location, effectiveRoles, true)
        return true
      }
    }

    logAccess(location, effectiveRoles, false)
    false
  }
  
  def logAccess(location: String, roles: Seq[Role], allowed: Boolean) = Props.mode match {
    case Props.RunModes.Production =>
    case _ => {
      val roleNames = roles map (_.name)
      info("ACL: url = %s | roles = %s | allowed = %s".format(location, roleNames.mkString(","), allowed.toString))
    }
  }

}