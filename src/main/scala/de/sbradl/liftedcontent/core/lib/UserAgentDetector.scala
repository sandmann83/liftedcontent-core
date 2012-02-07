package de.sbradl.liftedcontent.core.lib

import nl.bitwalker.useragentutils.UserAgent
import net.liftweb.common.Full
import net.liftweb.http.S
import nl.bitwalker.useragentutils.BrowserType
import net.liftweb.common.Box
import net.liftweb.common.Empty

object UserAgentDetector extends UserAgentDetector {

}

class UserAgentDetector {

  def isMobile = (detect.map(_.getBrowser.getBrowserType).openOr(BrowserType.UNKNOWN) ==
    BrowserType.MOBILE_BROWSER) || (S.request match {
      case Full(r) => r.isIPhone
      case _ => false
    })

  private def detect: Box[UserAgent] = S.request match {
    case Full(r) => {
      Full(UserAgent.parseUserAgentString(r.userAgent.openOr("")))
    }
    case _ => Empty
  }
}