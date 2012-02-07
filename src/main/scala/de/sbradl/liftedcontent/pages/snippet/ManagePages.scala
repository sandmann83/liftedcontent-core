package de.sbradl.liftedcontent.pages.snippet

import de.sbradl.liftedcontent.pages.model.{Page => PageModel}
import scala.xml.NodeSeq
import scala.xml.Text
import de.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Empty
import net.liftweb.mapper.By
import net.liftweb.util.Helpers._
import net.liftweb.http.S

class ManagePages {

  val pages = PageModel.findAll

  val user = User.currentUser.open_!

  def render = {
    "*" #> <ul>
             {
               pages map (renderPage(_))
             }
           </ul>
  }

  private def renderPage(page: PageModel): NodeSeq = {
    <li>
      { page.name } <small>({ S ? "TRANSLATED_INTO" }: { renderLinksToTranslations(page) }, <a href={"/page/translate/" + urlEncode(page.name)}>{ S ? "ADD_TRANSLATION" }</a>)</small>
    </li>
  }

  private def renderLinksToTranslations(page: PageModel): NodeSeq = (page.contents map {
    content =>
      {
        <a href={ content.url }>{ content.language.isAsLocale.getDisplayLanguage(user.locale.isAsLocale) }</a>
      }
  })

}