package eu.sbradl.liftedcontent.admin

import eu.sbradl.liftedcontent.admin.model.BackendArea
import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.util.Module

import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.LiftRules
import net.liftweb.http.ParsePath
import net.liftweb.http.RewriteRequest
import net.liftweb.http.RewriteResponse
import net.liftweb.http.S
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.Loc.If
import net.liftweb.sitemap.Loc.LocGroup
import net.liftweb.sitemap.Loc.strToFailMsg
import net.liftweb.sitemap.Loc
import net.liftweb.sitemap.Menu
import net.liftweb.util.NamedPF
import net.liftmodules.widgets.logchanger.Log4jLoggingBackend
import net.liftmodules.widgets.logchanger.LogLevelChanger

class AdminModule extends Module {

  object logLevel extends LogLevelChanger with Log4jLoggingBackend {
    override def menuLocParams: List[Loc.AnyLocParam] = List(User.testSuperUser, Hidden)
  }

  def menuTest = User.superUser_?

  override def init {
    LogLevelChanger.init
    BackendArea.insert("CHANGE_LOGLEVELS", "loglevels.png", "loglevel/change", "CHANGE_LOGLEVELS")

    LiftRules.statelessRewrite.prepend(NamedPF("LogLevelChangerRewrite") {
      case RewriteRequest(
        ParsePath("admin" :: "loglevel" :: "change" :: Nil, _, _, _), _, _) =>
        RewriteResponse("loglevel" :: "change" :: Nil)
    })
  }

  override def menus = List(
    Menu.i("ADMIN") / "admin" / "index" >> If(() => menuTest, S ? "NEED_TO_BE_ADMIN")
      >> LocGroup("primary") submenus (logLevel.menu :: BackendArea.menus))

}