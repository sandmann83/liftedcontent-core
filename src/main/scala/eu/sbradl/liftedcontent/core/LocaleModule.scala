package eu.sbradl.liftedcontent.core

import eu.sbradl.liftedcontent.util.Module
import net.liftweb.http.LiftRules
import net.liftweb.common.Box
import net.liftweb.http.provider.HTTPRequest
import net.liftweb.common.Full
import net.liftweb.http.provider.HTTPCookie
import java.util.Locale
import eu.sbradl.liftedcontent.core.model.User
import net.liftweb.http.S
import net.liftweb.http.Req
import model.SetupInformation

class LocaleModule extends Module {

  def name = "Locale"

  override def resourceNames = List("i18n/core")

  override def init {

    LiftRules.localeCalculator = {
      case fullReq @ Full(req) => {
        S.param("lang") match {
          case Full(null) => calcLocale(fullReq)
          case f @ Full(selectedLocale) => {
            S.addCookie(localeCookie(selectedLocale))
            new Locale(selectedLocale)
          }
          case _ => calcLocale(fullReq)
        }
      }
      case _ => Locale.getDefault

    }
  }

  private val languageCookie = "liftedcontent.language"
  private def localeCookie(in: String): HTTPCookie = HTTPCookie(languageCookie, in)

  private def localeFromString(in: String): Locale = {
    val x = in.split("_").toList
    new Locale(x.head, x.last)
  }

  private def calcLocale(req: Box[HTTPRequest]): Locale = SetupInformation.locale match {
    case Full(locale) => locale
    case _ => User.currentUser match {
      case Full(user) => user.locale.isAsLocale
      case _ => LiftRules.defaultLocaleCalculator(req)
    }
  }

}