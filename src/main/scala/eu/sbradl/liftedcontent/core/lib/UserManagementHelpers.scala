package eu.sbradl.liftedcontent.core.lib

import eu.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Full
import net.liftweb.common.Empty
import net.liftweb.http.js.JsCmd
import net.liftweb.mapper.By
import net.liftweb.http.js.JsCmds

object UserManagementHelpers {

  /**
   * Checks whether the current user is allowed to delete another user.
   *
   * @param user The user to check
   */
  def canDelete(user: User): Boolean = User.currentUser match {
    case Full(currentUser) => {
      if (currentUser.id.is == user.id.is) false
      else if (user.id.is == User.guestUser.id.is) false
      else true
    }
    case _ => false
  }
  
  def deleteUser(user: User): JsCmd = {
    user.delete_!
    
    UserServer ! UserAddedOrDeleted
  }
  
  def activateOrDeactivateUser(id: Long, status: Boolean): JsCmd = {
    val user = User.find(By(User.id, id)).openOr(return )
    user.validated(status)
    user.save

    JsCmds.Noop
  }

}