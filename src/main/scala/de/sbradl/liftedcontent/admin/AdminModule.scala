package de.sbradl.liftedcontent.admin

import de.sbradl.liftedcontent.admin.model.BackendArea
import de.sbradl.liftedcontent.core.model.User
import net.liftweb.http.S
import net.liftweb.sitemap.LocPath.stringToLocPath
import net.liftweb.sitemap.Loc.If
import net.liftweb.sitemap.Loc.LocGroup
import net.liftweb.sitemap.Loc.strToFailMsg
import net.liftweb.sitemap.Menu
import de.sbradl.liftedcontent.util.Module

class AdminModule extends Module {

  val name = "Admin"

  def menuTest = User.superUser_?

  override def menus = List(
    Menu.i("ADMIN") / "admin" / "index" >> If(() => menuTest, S ? "NEED_TO_BE_ADMIN")
      >> LocGroup("primary") submenus (BackendArea.menus))

}