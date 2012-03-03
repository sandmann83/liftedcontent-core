package eu.sbradl.liftedcontent.core.lib

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager

object UserServer extends LiftActor with ListenerManager {

  def createUpdate = UserAddedOrDeleted
  
  override def lowPriority = {
    case UserAddedOrDeleted => updateListeners
  }
  
}