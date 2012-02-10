package de.sbradl.liftedcontent.core.snippet

import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.common.Empty
import net.liftweb.common.Full
import de.sbradl.liftedcontent.core.model.User
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.ElemById
import net.liftweb.http.js.JsCmd
import de.sbradl.liftedcontent.core.model.{ SetupInformation => SetupInformationModel }
import net.liftweb.util.ClearNodes
import net.liftweb.http.S
import de.sbradl.liftedcontent.util.DatabaseHelpers._

class SetupInformation {

  val information = SetupInformationModel.find().openOr(
    SetupInformationModel.create
      .title("LiftedContent")
      .subtitle("Scala/Lift based CMS"))

  val enableEditing = User.superUser_?

  def render = {
    val titleId = nextFuncName
    val subtitleId = nextFuncName
    
    "data-lift-id=title [contenteditable]" #> enableEditing &
      "data-lift-id=title [id]" #> titleId &
      "data-lift-id=title [onblur]" #> SHtml.ajaxCall(ElemById(titleId, "innerHTML"), saveTitle _) &
      "data-lift-id=title *" #> information.title &
      "data-lift-id=subtitle [contenteditable]" #> enableEditing &
      "data-lift-id=subtitle [id]" #> subtitleId &
      "data-lift-id=subtitle [onblur]" #> SHtml.ajaxCall(ElemById(subtitleId, "innerHTML"), saveSubTitle _) &
      "data-lift-id=subtitle *" #> information.subtitle
  }

  private def saveTitle(text: String): JsCmd = {
    if(enableEditing) {
      information.title(text)
      
      save(information)
    }
  }
  
  private def saveSubTitle(text: String): JsCmd = {
    if(enableEditing) {
      information.subtitle(text)
      
      save(information)
    }
  }
}