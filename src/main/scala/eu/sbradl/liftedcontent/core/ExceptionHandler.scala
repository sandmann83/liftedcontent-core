package eu.sbradl.liftedcontent.core

import net.liftweb.http._
import net.liftweb.util.NamedPF
import net.liftweb.util.Props
import net.liftweb.common.Logger
import net.liftweb.common.Box
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.common.Loggable
import eu.sbradl.liftedcontent.util.Module

object ExceptionHandler extends Module with Logger {

  object lastError extends SessionVar[Box[Throwable]](Empty)

  override def init {
    initExceptionHandler
  }

  def logAndSetException(exception: Throwable) {
    error(exception.getMessage())
    error(exception.getStackTraceString)

    lastError(Full(exception))
  }

  private def initExceptionHandler = {
    LiftRules.exceptionHandler.prepend(NamedPF("Exception Handler") {
      case (_, Req(path, "", _), exception) => {
        logAndSetException(exception)
        RedirectResponse("/error")
      }
    })
  }
}