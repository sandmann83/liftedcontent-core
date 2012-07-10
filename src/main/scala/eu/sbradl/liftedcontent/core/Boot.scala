package eu.sbradl.liftedcontent.core

import eu.sbradl.liftedcontent.admin.model.BackendArea
import eu.sbradl.liftedcontent.admin.AdminModule
import eu.sbradl.liftedcontent.core.lib.ACL
import eu.sbradl.liftedcontent.core.lib.Database
import eu.sbradl.liftedcontent.core.lib.MailConfigurator
import eu.sbradl.liftedcontent.core.lib.ModuleManager
import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.pages.PagesModule
import eu.sbradl.liftedcontent.util.Module
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.Html5Properties
import net.liftweb.http.LiftRules
import net.liftweb.http.RedirectResponse
import net.liftweb.http.Req
import net.liftweb.http.S
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.Loc.If
import net.liftweb.sitemap.Loc.LocGroup
import net.liftweb.sitemap.Loc.strToFailMsg
import net.liftweb.sitemap.ConvertableToMenu
import net.liftweb.sitemap.Loc
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.SiteMap
import net.liftweb.util.Vendor.valToVender
import net.liftmodules._
import eu.sbradl.liftedcontent.util.UtilModule
import eu.sbradl.liftedcontent.rte.RichTextEditorModule
import eu.sbradl.liftedcontent.repository.ContentRepository

trait Boot {

  val adminModule = new AdminModule

  def modules: List[Module] = List(adminModule, UtilModule, new StartPageModule,
    new LocaleModule, ExceptionHandler, new ErrorPages, SetupModule,
    new UserModule, new AjaxModule, new PermissionModule, new MetaTagModule,
    new PagesModule, new RichTextEditorModule, ContentRepository, ContentRepositoryHelper)
    
  def additionalMenus: List[ConvertableToMenu] = List()

  def boot {
    MailConfigurator.init
    JQueryModule.init
    registerModules
    initModules

    val isInstalled = () => SetupModule.isAlreadyInstalled
    val isNotInstalled = () => !SetupModule.isAlreadyInstalled

    val menus: List[ConvertableToMenu] = List(
      Menu.i("SETUP") / "setup" >> If(isNotInstalled, S ? "ALREADY_INSTALLED") >> LocGroup("primary"),
      Menu.i("HOME") / "index" >> If(isInstalled, "") >> LocGroup("primary"),
      Menu.i("MOBILE_HOME") / "mobile" >> Hidden >> LocGroup("primary"),

      Menu.i("CONTACT") / "contact" >> LocGroup("secondary"),
      //        Menu.i("SITEMAP") / "sitemap" >> LocGroup("secondary"),
      Menu.i("ERROR") / "error") ::: ModuleManager.menus ::: additionalMenus

    def sitemap = new SiteMap(List({
      case r => If(() => ACL.isAllowed(r), RedirectResponse("/user_mgt/login"))
    }), menus: _*)

    def sitemapMutators = User.sitemapMutator

    LiftRules.setSiteMapFunc(() => sitemapMutators(sitemap))

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    LiftRules.fixCSS("css" :: "default" :: Nil, Empty)

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
