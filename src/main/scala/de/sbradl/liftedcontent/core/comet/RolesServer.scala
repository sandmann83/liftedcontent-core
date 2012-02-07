package de.sbradl.liftedcontent.core.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager

object RolesServer extends LiftActor with ListenerManager {
  
  def createUpdate = RolesChanged
  
  override def lowPriority = {
    case RolesChanged => updateListeners
  }
  
}