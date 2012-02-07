package de.sbradl.liftedcontent.core.snippet

import net.liftweb.common.Full
import net.liftweb.common.Empty
import net.liftweb.http.S
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.util.Props

import de.sbradl.liftedcontent.core.ExceptionHandler

class LastError {

  def show(xhtml: NodeSeq) = {
    bind("error", xhtml,
      "title" -> title,
      "message" -> message,
      "stacktrace" -> stacktrace)
  }

  private def title = Props.mode match {
    case Props.RunModes.Production => {
      S ? "AN_ERROR_OCCURED"
    }
    case _ => ExceptionHandler.lastError.is match {
      case Full(exception) => exception.getClass().getName()
      case _ => S ? "NO_ERROR"
    }
  }

  private def message = ExceptionHandler.lastError.is match {
    case Full(exception) => S ? exception.getLocalizedMessage()
    case _ => ""
  }

  private def stacktrace = Props.mode match {
    case Props.RunModes.Production => {
      ""
    }
    case _ => ExceptionHandler.lastError.is match {
      case Full(exception) => exception.getStackTraceString
      case _ => ""
    }
  }

}