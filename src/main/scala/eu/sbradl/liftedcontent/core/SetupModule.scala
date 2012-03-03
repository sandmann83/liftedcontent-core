package eu.sbradl.liftedcontent.core

import eu.sbradl.liftedcontent.util.Module
import net.liftweb.http.LiftRules
import net.liftweb.http.Req
import net.liftweb.common.Full
import net.liftweb.http.RedirectResponse
import eu.sbradl.liftedcontent.core.model.SetupInformation
import net.liftweb.util.NamedPF
import eu.sbradl.liftedcontent.core.lib.Database
import eu.sbradl.liftedcontent.admin.model.BackendArea
import net.liftweb.mapper.By
import net.liftweb.http.S
import eu.sbradl.liftedcontent.core.model.User

object SetupModule extends Module {
  
  def name = "Setup"
  
  def isAlreadyInstalled = User.count > 0
  
  override def mappers = List(SetupInformation)
  
  override def init {
    LiftRules.dispatch.prepend(NamedPF("Setup Check") {
      case Req("setup" :: _, "", _) if isAlreadyInstalled =>
        () => throw new Exception(S ? "LIFTEDCONTENT_ALREADY_INSTALLED")
    })

    LiftRules.dispatch.prepend(NamedPF("Setup Check") {
      case Req(location, "", _) if (!isAlreadyInstalled && location.head != "setup"
        && location.head != "error") =>
        () => Full(RedirectResponse("/setup"))
    })
    
    BackendArea.insert("GENERAL_SETTINGS", "general.png", "general", "GENERAL_SETTINGS_DESCRIPTION")
  }

}