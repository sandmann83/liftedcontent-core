package de.sbradl.liftedcontent.core

import de.sbradl.liftedcontent.util.Module
import net.liftweb.common.Full
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.LiftRules

class AjaxModule extends Module {
  
  def name = "Ajax"

  override def init {
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
  }

}