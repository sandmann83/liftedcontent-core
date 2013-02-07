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
import net.liftweb.util.Helpers.nextFuncName
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.StringPromotable.booleanToStrPromo
import net.liftweb.util.StringPromotable.jsCmdToStrPromo
import net.liftmodules.textile.TextileParser

class Page(p: PageContent) {

  private def displayContent: String = p.text.is match {
    case null => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
    case "" => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
    case _ => p.text
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
    val titleId = nextFuncName
    val contentId = nextFuncName

    val enableEditing = User.superUser_?

    "data-lift-id=title *" #> p.title &
      "data-lift-id=title [id]" #> titleId &
      "data-lift-id=title [contenteditable]" #> enableEditing &
      "data-lift-id=title [onblur]" #> SHtml.ajaxCall(ElemById(titleId, "innerHTML"), saveTitle _) &
      "data-lift-id=editor" #> DisplayIf((enableEditing)) &
      "data-lift-id=content [id]" #> contentId &
      "data-lift-id=content [class+]" #> (if (User.superUser_?) { "editable" } else { "" }) &
      "data-lift-id=content *" #> (if (enableEditing) Text(displayContent) else TextileParser.toHtml(displayContent)) &
      "data-lift-id=save [onclick]" #> SHtml.ajaxCall(ElemById(contentId, "value"), saveContent _)
  }
}