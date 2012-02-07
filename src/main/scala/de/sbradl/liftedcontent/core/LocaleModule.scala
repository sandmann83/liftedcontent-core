package de.sbradl.liftedcontent.core

import de.sbradl.liftedcontent.util.Module
import net.liftweb.http.LiftRules
import net.liftweb.common.Box
import net.liftweb.http.provider.HTTPRequest
import net.liftweb.common.Full
import net.liftweb.http.provider.HTTPCookie
import java.util.Locale
import de.sbradl.liftedcontent.core.model.User
import net.liftweb.http.S

class LocaleModule extends Module {

  def name = "Locale"

  override def resourceNames = List("i18n/core")

  override def init {
    val languageCookie = "liftedcontent.language"

    LiftRules.localeCalculator = {
      case fullReq @ Full(req) => {
        def localeCookie(in: String): HTTPCookie = HTTPCookie(languageCookie, in)

        def localeFromString(in: String): Locale = {
          val x = in.split("_").toList; new Locale(x.head, x.last)
        }

        def calcLocale: Locale = User.currentUser match {
          case Full(user) => user.locale.isAsLocale
          case _ => LiftRules.defaultLocaleCalculator(fullReq)
        }

        S.param("lang") match {
          case Full(null) => calcLocale
          case f @ Full(selectedLocale) => {
            S.addCookie(localeCookie(selectedLocale))
            new Locale(selectedLocale)
          }
          case _ => calcLocale
        }
      }
      case _ => Locale.getDefault

    }
  }

}