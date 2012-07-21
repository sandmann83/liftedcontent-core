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

class Page(p: PageContent) {

  private def displayContent: String = p.text.is match {
    case null => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
    case "" => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
    case _ => p.text.is
  }

  def saveContent(value: String): JsCmd = {
    p.text(value)
    p.save

    S.notice(S ? "SAVED_CHANGES")
  }

  def saveTitle(title: String): JsCmd = {
    p.title(title)
    p.save

    S.notice(S ? "SAVED_CHANGES")
  }

  def render = {
    "data-lift-id=title *" #> p.title &
      "#page-content-1 *" #> displayContent
  }
}