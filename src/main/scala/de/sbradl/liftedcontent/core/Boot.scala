package de.sbradl.liftedcontent.core

import de.sbradl.liftedcontent.core.lib.ACL
import de.sbradl.liftedcontent.core.lib.Database
import de.sbradl.liftedcontent.core.lib.ModuleManager
import de.sbradl.liftedcontent.core.model.User
import de.sbradl.liftedcontent.util.Module
import net.liftweb.common.Full
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.Html5Properties
import net.liftweb.http.LiftRules
import net.liftweb.http.RedirectResponse
import net.liftweb.http.Req
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.Loc.If
import net.liftweb.sitemap.Loc.LocGroup
import net.liftweb.sitemap.Loc.strToFailMsg
import net.liftweb.sitemap.ConvertableToMenu
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.SiteMap
import net.liftweb.util.Vendor.valToVender
import net.liftweb.util.NamedPF
import net.liftweb.http.S
import de.sbradl.liftedcontent.admin.AdminModule
import de.sbradl.liftedcontent.pages.PagesModule
import de.sbradl.liftedcontent.admin.model.BackendArea
import de.sbradl.liftedcontent.rte.RichTextEditorModule
import net.liftweb.common.Empty
import de.sbradl.liftedcontent.core.lib.MailConfigurator

trait Boot {

  val adminModule = new AdminModule

  def modules: List[Module] = List(adminModule, new StartPageModule,
      new LocaleModule, ExceptionHandler, new ErrorPages, SetupModule, 
      new UserModule, new AjaxModule, new PermissionModule, 
      new PagesModule, new RichTextEditorModule)

  def boot {

    try {
      MailConfigurator.init
      registerModules
      initModules

      val isInstalled = () => SetupModule.isAlreadyInstalled
      val isNotInstalled = () => !SetupModule.isAlreadyInstalled

      val menus: List[ConvertableToMenu] = List(
        Menu.i("SETUP") / "setup" / "index" >> If(() => { isNotInstalled() || User.superUser_? }, S ? "NEED_TO_BE_ADMIN") >> LocGroup("primary") submenus (
          (Menu.i("SETUP_BASIC") / "setup" / "basic" >> If(isNotInstalled, S ? "ALREADY_INSTALLED") >> LocGroup("primary")) ::
          ModuleManager.setupMenus),
        Menu.i("HOME") / "index" >> If(isInstalled, "") >> LocGroup("primary"),
        Menu.i("MOBILE_HOME") / "mobile" >> Hidden >> LocGroup("primary"),

        Menu.i("CONTACT") / "contact" >> LocGroup("secondary"),
//        Menu.i("SITEMAP") / "sitemap" >> LocGroup("secondary"),
        Menu.i("ERROR") / "error" / "index" submenus (
          Menu.i("BOOT_ERROR") / "error" / "boot")) ::: ModuleManager.menus

      def sitemap = new SiteMap(List({
        case r => If(() => ACL.isAllowed(r), RedirectResponse("/user_mgt/login"))
      }), menus: _*)

      def sitemapMutators = User.sitemapMutator

      LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))

      LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

      LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts

      LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

      LiftRules.loggedInTest = Full(() => User.loggedIn_?)
      
      LiftRules.fixCSS("css" :: "default" :: Nil, Empty)

    } catch {
      case ex: Exception => {
        LiftRules.dispatch.prepend(NamedPF("Initialization error") {
          case Req(location, "", _) if (location != List("error", "boot")) =>
            () => {
              ExceptionHandler.logAndSetException(ex)
              Full(RedirectResponse("/error/boot"))
            }
        })
      }
    }
  }

  private def registerModules {
    modules foreach (ModuleManager.register(_))
  }

  private def initModules {
    Database.addMapper(BackendArea)
    Database.init
    ModuleManager.init
  }
}