package eu.sbradl.liftedcontent.core

import net.liftweb.http.LiftRules
import net.liftweb.util.NamedPF
import net.liftweb.http.NotFoundAsTemplate
import net.liftweb.http.ParsePath
import eu.sbradl.liftedcontent.util.Module

/**
 * Set error pages.
 */
class ErrorPages extends Module {
  
  override def init {
    LiftRules.uriNotFound.prepend(NamedPF("404 Handler") {
      case (req, failure) =>
        NotFoundAsTemplate(ParsePath(List("404"), "html", false, false))
    })
  }
}