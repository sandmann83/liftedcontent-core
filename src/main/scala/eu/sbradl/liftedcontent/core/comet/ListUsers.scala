package eu.sbradl.liftedcontent.core.comet

import eu.sbradl.liftedcontent.core.lib.UserManagementHelpers.activateOrDeactivateUser
import eu.sbradl.liftedcontent.core.lib.UserManagementHelpers.canDelete
import eu.sbradl.liftedcontent.core.lib.UserManagementHelpers.deleteUser
import eu.sbradl.liftedcontent.core.lib.UserAddedOrDeleted
import eu.sbradl.liftedcontent.core.lib.UserServer
import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.util.OnConfirm

import net.liftweb.http.CometActor
import net.liftweb.http.CometListener
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.mapper.MappedField.mapToType
import net.liftweb.util.IterableConst.itNodeSeqFunc
import net.liftweb.util.StringPromotable.jsCmdToStrPromo
import net.liftweb.util.ClearNodes
import net.liftweb.util.PassThru

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
  
  override def lowPriority = {
    case UserAddedOrDeleted => reRender
  }
  
}