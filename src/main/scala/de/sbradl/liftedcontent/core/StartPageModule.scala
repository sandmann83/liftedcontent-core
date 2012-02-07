package de.sbradl.liftedcontent.core

import de.sbradl.liftedcontent.util.Module
import net.liftweb.http.LiftRules
import net.liftweb.util.NamedPF
import net.liftweb.http.Req
import net.liftweb.http.S
import net.liftweb.common.Full
import net.liftweb.http.RedirectResponse
import de.sbradl.liftedcontent.core.model.StartPage
import lib.UserAgentDetector
import de.sbradl.liftedcontent.core.model.MobileStartPage

class StartPageModule extends Module {
  
  def name = "StartPage"

  override def mappers = List(StartPage, MobileStartPage)

  override def init {
    LiftRules.dispatch.prepend(NamedPF("StartPage Redirector") {
      case Req(List("index"), "", _) if (!StartPage.find().isEmpty && StartPage.find().openTheBox.url.is != "index") =>
        () => UserAgentDetector.isMobile match {
          case true => Full(RedirectResponse(MobileStartPage.find().map(_.url.is).openOr("/mobile")))
          case false => Full(RedirectResponse(StartPage.find().map(_.url.is).openOr("/")))
        }
    })

    StartPage.insert("index")
  }

}