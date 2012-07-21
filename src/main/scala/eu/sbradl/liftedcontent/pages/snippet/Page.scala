package eu.sbradl.liftedcontent.pages.snippet

import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.pages.model.PageContent
import eu.sbradl.liftedcontent.util.DisplayIf
import scala.xml.Text
import net.liftweb.http.js.JE.ElemById
import net.liftweb.http.js.JsCmd.unitToJsCmd
import net.liftweb.http.js.JsCmd
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.textile.TextileParser
import net.liftweb.util.Helpers.nextFuncName
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.StringPromotable.booleanToStrPromo
import net.liftweb.util.StringPromotable.jsCmdToStrPromo
import net.liftweb.util.Helpers._
import eu.sbradl.liftedcontent.pages.model.PageRegion
import net.liftweb.util.CssSel
import net.liftweb.util.PassThru

class Page(p: PageContent) {

  //  private def displayContent: String = p.text.is match {
  //    case null => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
  //    case "" => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
  //    case _ => p.text.is
  //  }

  //  def saveContent(value: String): JsCmd = {
  //    p.text(value)
  //    p.save
  //
  //    S.notice(S ? "SAVED_CHANGES")
  //  }

  //  def saveTitle(title: String): JsCmd = {
  //    p.title(title)
  //    p.save
  //
  //    S.notice(S ? "SAVED_CHANGES")
  //  }

  def render = {
    val selectors = p.regions.all.map {
      region =>
        {
          "#page-content-%s *".format(region.name.is) #> region.text.is
        }
    }

    "data-lift-id=page-title [id]" #> p.id.is &
      "data-lift-id=page-title *" #> p.title.is &
      createSelector(selectors)
  }

  private def createSelector(selectors: List[CssSel]): CssSel = {
    def createSelector(selector: CssSel, selectors: List[CssSel]): CssSel = selectors match {
      case List() => selector
      case head :: tail => createSelector(selector & head, tail)
    }

    selectors match {
      case List() => "data-lift-id=content" #> PassThru
      case _ => createSelector(selectors.head, selectors.tail)
    }
  }

}