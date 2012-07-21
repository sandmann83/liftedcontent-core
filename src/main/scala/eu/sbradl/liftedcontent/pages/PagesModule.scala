package eu.sbradl.liftedcontent.pages

import eu.sbradl.liftedcontent.admin.model.BackendArea
import eu.sbradl.liftedcontent.pages.model.Page
import eu.sbradl.liftedcontent.pages.model.PageContent
import eu.sbradl.liftedcontent.util.Module
import scala.xml.Text
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.mapper.By
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.Loc
import net.liftweb.sitemap.Menu
import net.liftweb.util.Helpers.urlDecode
import net.liftweb.http.LiftRules
import eu.sbradl.liftedcontent.pages.lib.PageServices
import eu.sbradl.liftedcontent.core.model.User
import net.liftweb.http.Req
import net.liftweb.util.Helpers._ 

class PagesModule extends Module {

  override def mappers = List(Page, PageContent)

  override def menus = List(
    Menu.param[PageContent]("PAGE", new Loc.LinkText(p => Text(p.title.is)), url => PageContent.find(By(PageContent.title, urlDecode(url))), p => p.encodedTitle) / "page",
    Menu.i("ADD_PAGE") / "admin" / "add-page" >> Hidden,
    Menu.param[Page]("TRANSLATE_PAGE", new Loc.LinkText(p => Text(p.name.is)),
      name => Page.find(By(Page.name, urlDecode(name))), p => p.encodedName) / "page" / "translate")

  override def init {
    BackendArea.insert("MANAGE_PAGES", "pages.png", id, "MANAGE_PAGES")

    val onlyAsSuperUser: PartialFunction[Req, Unit] = {
      case _ if User.currentUser.map(_.superUser.is).openOr(false) =>
    }

    LiftRules.dispatch.append(onlyAsSuperUser guard PageServices)
  }
}