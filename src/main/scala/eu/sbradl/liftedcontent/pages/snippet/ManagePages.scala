package eu.sbradl.liftedcontent.pages.snippet

import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.pages.model.{ Page => PageModel }
import eu.sbradl.liftedcontent.pages.model.PageContent

import scala.xml.NodeSeq.seqToNodeSeq
import scala.xml.NodeSeq
import scala.xml.Unparsed

import net.liftweb.http.S
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.Helpers.urlEncode

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
      { page.name }<small>{ translatedInto(page) } - <a href={ "/page/translate/" + urlEncode(page.name) }>{ S ? "ADD_TRANSLATION" }</a></small>
    </li>
  }

  private def translatedInto(page: PageModel): NodeSeq = {
    val contents = page.contents

    contents.isEmpty match {
      case true => NodeSeq.Empty
      case false => <span>
                      ({ S ? "TRANSLATED_INTO" }: { Unparsed(renderLinksToTranslations(contents).mkString(", ")) })
                    </span>
    }

  }

  private def renderLinksToTranslations(contents: Seq[PageContent]): NodeSeq = contents map {
    content =>
      {
        <a href={ S.contextPath + content.url }>{ content.language.isAsLocale.getDisplayLanguage(user.locale.isAsLocale) }</a>
      }
  }

}