package de.sbradl.liftedcontent.pages.snippet

import scala.xml.Unparsed
import de.sbradl.liftedcontent.core.model.User
import de.sbradl.liftedcontent.pages.model.PageContent
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmd.unitToJsCmd
import net.liftweb.http.js.JsCmd
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.util.Helpers._
import net.liftweb.util.StringPromotable.booleanToStrPromo
import net.liftweb.http.js.JE.ElemById
import net.liftweb.http.js.JsCmds
import de.sbradl.liftedcontent.rte.snippet.RichTextEditor
import net.liftweb.util.PassThru
import scala.xml.NodeSeq
import net.liftweb.util.ClearNodes

class Page(p: PageContent) {

  private def displayContent: String = p.text.is match {
    case null => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
    case "" => S ? ("CLICK_TO_EDIT", p.language.isAsLocale)
    case _ => p.text
  }
  
  def onBlur(text: String): JsCmd = {
    p.text(text)
    p.save

    S.notice(S ? "SAVED_CHANGES")
  }

  def render = {
    val id = nextFuncName
    
    val enableEditing = User.superUser_?

    "data-lift-id=title *" #> p.title &
    "data-lift-id=editor" #> (if(enableEditing) PassThru else ClearNodes) &
      "data-lift-id=content [id]" #> id &
      "data-lift-id=content [contenteditable]" #> User.superUser_? &
      "data-lift-id=content [class+]" #> (if (User.superUser_?) {"editable"} else {""}) &
      "data-lift-id=content *" #> Unparsed(displayContent) &
      "data-lift-id=content [onblur]" #> SHtml.ajaxCall(ElemById(id, "innerHTML"), onBlur _)
  }
}