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
import net.liftweb.http.SessionVar
import net.liftweb.util.Helpers

class LocaleModule extends Module {

  override def resourceNames = List("i18n/core")

  override def init {

    LiftRules.localeCalculator = localeCalculator
  }

  private def localeCalculator(request: Box[HTTPRequest]): Locale = {

    object sessionLanguage extends SessionVar[Locale](LiftRules.defaultLocaleCalculator(request))

    request.flatMap(r => {
      def localeFromString(in: String): Locale = {
        val x = in.split("_").toList;
        sessionLanguage(new Locale(x.head, x.last))
        sessionLanguage.is
      }

      def calcLocale: Box[Locale] = User.currentUser match {
        case Full(user) => Full(user.locale.isAsLocale)
        case _ => Full(sessionLanguage.is)
      }

      S.param("locale") match {
        case Full(null) => calcLocale
        case Full(selectedLocale) => Helpers.tryo(localeFromString(selectedLocale))
        case _ => calcLocale
      }
    }).openOr(sessionLanguage.is)
  }

}