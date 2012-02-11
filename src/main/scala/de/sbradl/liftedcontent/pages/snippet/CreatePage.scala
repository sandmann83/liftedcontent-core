package de.sbradl.liftedcontent.pages.snippet

import de.sbradl.liftedcontent.core.model.User
import de.sbradl.liftedcontent.pages.model.{Page => PageModel}
import net.liftweb.util.AnyVar.whatVarIs
import net.liftweb.http.js.JsCmd
import de.sbradl.liftedcontent.util.DatabaseHelpers._
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.S

class CreatePage {
  
  def render = {
    var name = ""

    def process(): JsCmd = {
      val page = PageModel.create
      page.name(name)
      page.author(User.currentUser.open_!)
      
      save(page)
      
      S.redirectTo("/admin/pages")
    }

    "name=name" #> SHtml.text(name, name = _) &
    "name=submit" #> SHtml.ajaxSubmit(S ? "ADD_PAGE", process)
  }
  
}