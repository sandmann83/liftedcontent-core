package eu.sbradl.liftedcontent.pages.snippet

import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.pages.model.{ Page => PageModel }
import net.liftweb.util.AnyVar.whatVarIs
import net.liftweb.http.js.JsCmd
import eu.sbradl.liftedcontent.util.DatabaseHelpers._
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.S
import net.liftweb.http.LiftSession
import net.liftweb.common.Full

class CreatePage {

  def render = {
    var name = ""
    var template = ""

    def process(): JsCmd = {
      val page = PageModel.create
      
      page.name(name)
      page.template(template)
      page.author(User.currentUser.open_!)

      save(page)

      S.redirectTo("/admin/pages")
    }

    "name=name" #> SHtml.text(name, name = _) &
      "name=template" #> SHtml.select(availableTemplates, Full("page-default"), template = _) &
      "name=submit" #> SHtml.ajaxSubmit(S ? "ADD_PAGE", process)
  }

  private def availableTemplates = List(
    ("page-default", "page-default"))

}