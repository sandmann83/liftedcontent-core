package eu.sbradl.liftedcontent.core.snippet

import net.liftweb.common.Full
import net.liftweb.common.Empty
import net.liftweb.http.S
import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import eu.sbradl.liftedcontent.core.ExceptionHandler
import net.liftweb.util.ClearNodes
import scala.xml.Text

class LastError {

  def render = {
    "data-lift-id=title *" #> title &
      "data-lift-id=message *" #> message &
      "data-lift-id=stacktrace *" #> (if (Props.mode == Props.RunModes.Development) Full(stacktrace) else Empty)
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

  private def stacktrace = ExceptionHandler.lastError.is match {
    case Full(exception) => exception.getStackTraceString
    case _ => ""
  }

}