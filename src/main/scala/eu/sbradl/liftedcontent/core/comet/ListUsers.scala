package eu.sbradl.liftedcontent.core.comet

import eu.sbradl.liftedcontent.core.lib.UserAddedOrDeleted
import eu.sbradl.liftedcontent.core.lib.UserServer
import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.util.OnConfirm
import net.liftweb.http.js.JsCmd.unitToJsCmd
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds
import net.liftweb.http.CometActor
import net.liftweb.http.CometListener
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.mapper.By
import net.liftweb.util.IterableConst.itNodeSeqFunc
import net.liftweb.util.StringPromotable.jsCmdToStrPromo
import net.liftweb.util.PassThru
import net.liftweb.util.ClearNodes

class ListUsers extends CometActor with CometListener {
  
  def registerWith = UserServer

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
            "data-lift-id=delete" #> (if(canDelete(user)) PassThru else ClearNodes) &
            "data-lift-id=delete [onclick]" #> OnConfirm(S ? "REALLY_DELETE_USER", () => deleteUser(user)) &
            "data-lift-id=delete [title]" #> (S ? "DELETE")
        }
    }
  }
  
  private def canDelete(user: User): Boolean = {
    if (User.currentUser.open_!.id.is == user.id.is) false
    else if(user.id.is == User.guestUser.id.is) false
    else true
  }
  
  override def lowPriority = {
    case UserAddedOrDeleted => reRender
  }
  
  private def activateOrDeactivateUser(id: Long, status: Boolean): JsCmd = {
    val user = User.find(By(User.id, id)).openOr(return )
    user.validated(status)
    user.save

    JsCmds.Noop
  }
  
  private def deleteUser(user: User): JsCmd = {
    user.delete_!
    
    UserServer ! UserAddedOrDeleted
  }
}