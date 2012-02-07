package de.sbradl.liftedcontent.core

import net.liftweb.http.LiftRules
import net.liftweb.util.NamedPF
import net.liftweb.http.NotFoundAsTemplate
import net.liftweb.http.ParsePath
import de.sbradl.liftedcontent.util.Module

/**
 * Set error pages.
 */
class ErrorPages extends Module {

  def name = "ErrorPages"
  
  override def init {
    LiftRules.uriNotFound.prepend(NamedPF("404 Handler") {
      case (req, failure) =>
        NotFoundAsTemplate(ParsePath(List("404"), "html", false, false))
    })
  }
}