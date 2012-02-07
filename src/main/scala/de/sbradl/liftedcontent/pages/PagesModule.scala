package de.sbradl.liftedcontent.pages

import scala.xml.Text
import de.sbradl.liftedcontent.admin.model.BackendArea
import de.sbradl.liftedcontent.pages.model.Page
import de.sbradl.liftedcontent.pages.model.PageContent
import de.sbradl.liftedcontent.util.Module
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.mapper.By
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.Loc
import net.liftweb.sitemap.Menu
import net.liftweb.util.Helpers.urlDecode
import de.sbradl.liftedcontent.core.lib.ACL
import net.liftweb.sitemap.Loc.Test

class PagesModule extends Module {

  def name = "Pages"

  override def mappers = List(Page, PageContent)

  override def menus = List(
      Menu.param[PageContent]("PAGE", new Loc.LinkText(p => Text(p.title)), url => PageContent.find(By(PageContent.title, urlDecode(url))), p => p.encodedTitle) / "page",
      Menu.i("ADD_PAGE") / "admin" / "add-page" >> Hidden,
      Menu.param[Page]("TRANSLATE_PAGE", new Loc.LinkText(p => Text(p.name)), 
          name => Page.find(By(Page.name, urlDecode(name))), p => p.encodedName) / "page" / "translate")

  override def init {
    BackendArea.insert("MANAGE_PAGES", "pages.png", id, "MANAGE_PAGES")
  }

}