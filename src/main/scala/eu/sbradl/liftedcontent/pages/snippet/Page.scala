package eu.sbradl.liftedcontent.pages.snippet

import scala.xml.Unparsed
import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.pages.model.PageContent
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmd.unitToJsCmd
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.util.Helpers._
import net.liftweb.util.StringPromotable.booleanToStrPromo
import net.liftweb.http.js.JE.ElemById
import net.liftweb.http.js.JsCmds
import eu.sbradl.liftedcontent.rte.snippet.RichTextEditor
import net.liftweb.util.PassThru
import scala.xml.NodeSeq
import net.liftweb.util.ClearNodes
import net.liftweb.http.js.JE.JsEq
import net.liftweb.http.js.JE.JsArray
import net.liftweb.textile.TextileParser
import scala.xml.Text

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
      "data-lift-id=editor" #> (if (enableEditing) PassThru else ClearNodes) &
      "data-lift-id=content [id]" #> contentId &
      "data-lift-id=content [class+]" #> (if (User.superUser_?) { "editable" } else { "" }) &
      "data-lift-id=content *" #> (if (enableEditing) Text(displayContent) else TextileParser.toHtml(displayContent)) &
      "data-lift-id=save [onclick]" #> SHtml.ajaxCall(ElemById(contentId, "value"), saveContent _)
  }
}