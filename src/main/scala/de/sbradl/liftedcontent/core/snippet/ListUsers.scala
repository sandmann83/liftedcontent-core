package de.sbradl.liftedcontent.core.snippet

import de.sbradl.liftedcontent.core.model.User
import net.liftweb.http.SHtml.BasicElemAttr
import net.liftweb.http.js.JsCmd.unitToJsCmd
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.mapper.By
import net.liftweb.util.Helpers.strToCssBindPromoter
import net.liftweb.util.IterableConst.itNodeSeqFunc
import de.sbradl.liftedcontent.util.OnConfirm

class ListUsers {

  def activateOrDeactivateUser(id: Long, status: Boolean): JsCmd = {
    val user = User.find(By(User.id, id)).openOr(return )
    user.validated(status)
    user.save

    JsCmds.Noop
  }
  
  def deleteUser(user: User): JsCmd = {
    user.delete_!
    
    JsCmds.Noop
  }

  def render = {
    val users = User.findAll

    "data-lift-id=userdata *" #> users.map {
      user =>
        {
          "data-lift-id=email *" #> user.email.is &
            "data-lift-id=firstname *" #> user.firstName &
            "data-lift-id=lastname *" #> user.lastName &
            "data-lift-id=status *" #> SHtml.ajaxCheckbox(user.validated, (value) => activateOrDeactivateUser(user.id.is, value)) &
            "data-lift-id=roles [href]" #> ("/admin/users/manage-roles/" + user.id.is) &
            "data-lift-id=roles [title]" #> (S ? "MANAGE_ROLES") &
            "data-lift-id=delete [onclick]" #> OnConfirm(S ? "REALLY_DELETE_USER", () => deleteUser(user)) &
            "data-lift-id=delete [title]" #> (S ? "DELETE")
        }
    }
  }
}