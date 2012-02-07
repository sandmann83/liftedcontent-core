package de.sbradl.liftedcontent.core

import de.sbradl.liftedcontent.util.Module
import net.liftweb.http.LiftRules
import net.liftweb.http.Req
import net.liftweb.common.Full
import net.liftweb.http.RedirectResponse
import de.sbradl.liftedcontent.core.model.SetupInformation
import net.liftweb.util.NamedPF
import de.sbradl.liftedcontent.core.lib.Database
import de.sbradl.liftedcontent.admin.model.BackendArea
import net.liftweb.mapper.By
import net.liftweb.http.S

object SetupModule extends Module {
  
  def name = "Setup"
  
  def isAlreadyInstalled = SetupInformation.count > 0
  
  override def mappers = List(SetupInformation)
  
  override def init {
    LiftRules.dispatch.prepend(NamedPF("Setup Check") {
      case Req("setup" :: "basic" :: _, "", _) if isAlreadyInstalled =>
        () => throw new Exception(S ? "LIFTEDCONTENT_ALREADY_INSTALLED")
    })

    LiftRules.dispatch.prepend(NamedPF("Setup Check") {
      case Req(location, "", _) if (!isAlreadyInstalled && location.head != "setup"
        && location.head != "error") =>
        () => Full(RedirectResponse("/setup/basic"))
    })
    
    BackendArea.insert("GENERAL_SETTINGS", "general.png", "general", "GENERAL_SETTINGS_DESCRIPTION")
  }

}