package de.sbradl.liftedcontent.core.comet

import net.liftweb.http.CometListener
import net.liftweb.http.CometActor
import de.sbradl.liftedcontent.core.model.Role
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.ElemById
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds
import net.liftweb.http.S
import de.sbradl.liftedcontent.util.OnConfirm

class Roles extends CometActor with CometListener {
  
  def registerWith = RolesServer

  def save(role: Role)(x: Any): JsCmd = {
    role.name(x.toString)
    role.save

    S.notice(S ? "SAVED_CHANGES")
  }

  def delete(role: Role): JsCmd = {
    role.delete_!

    RolesServer ! RolesChanged
    
    S.notice(S ? "ROLE_DELETED")
  }
  
  def render = {
    val roles = Role.findAll

    "data-lift-id=roledata *" #> roles.map {
      role =>
        {
          val id = nextFuncName
          "data-lift-id=name *" #> role.name &
            "data-lift-id=name [contenteditable]" #> true &
            "data-lift-id=name [id]" #> id &
            "data-lift-id=name [onblur]" #> SHtml.ajaxCall(ElemById(id, "innerHTML"), save(role) _) &
            "data-lift-id=delete [onclick]" #> OnConfirm(S ? "REALLY_DELETE_ROLE", () => delete(role)) & // SHtml.onEvent(s => JsCmds.Confirm(S ? "REALLY_DELETE_ROLE", SHtml.ajaxInvoke(() => delete(role))._2.cmd)) &
            "data-lift-id=delete [title]" #> (S ? "DELETE_ROLE") &
            "data-lift-id=delete [style]" #> {
              if (Role.isDefaultRole(role)) {
                "display: none;"
              } else {
                ""
              }
            }
        }
    }
  }
  
  override def lowPriority: PartialFunction[Any, Unit] = {
    case _ => reRender
  }
  
}