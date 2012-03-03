package eu.sbradl.liftedcontent.core.lib

import java.util.Locale
import net.liftweb.http.S
import net.liftweb.http.LiftScreen

object LocaleHelpers {

  def languageNames = (Locale.getAvailableLocales map {
    locale => locale.getDisplayLanguage(S.locale)
  } distinct) sorted

  def languages: Seq[(String, String)] = (Locale.getAvailableLocales map {
    locale => (locale.getLanguage, locale.getDisplayLanguage(S.locale))
  } distinct) sorted

  def languagesPromoter = (kv: (String, String)) => kv._2

  def languageName(isoCode: String) = try {
    Locale.forLanguageTag(isoCode).getDisplayLanguage(S.locale)
  } catch {
    case e: NullPointerException => S ? "MULTILINGUAL"
  }

}