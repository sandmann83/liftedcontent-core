package de.sbradl.liftedcontent.core.snippet

import de.sbradl.liftedcontent.core.model.StartPage
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.LiftRules
import net.liftweb.http.LiftScreen
import net.liftweb.http.S
import net.liftweb.sitemap.SiteMap
import de.sbradl.liftedcontent.core.model.{ SetupInformation => SetupInformationModel }
import de.sbradl.liftedcontent.core.lib.LocaleHelpers

class GeneralSettings extends LiftScreen {

  override def defaultToAjax_? = true

  override def finishButton = <button>{ S ? "SAVE" }</button>
  override def cancelButton = <button>{ S ? "CANCEL" }</button>

  object startPage extends ScreenVar(StartPage.find().openOr(StartPage.create))
  object setupInfo extends ScreenVar(SetupInformationModel.findOrDefault)

  val urls = LiftRules.siteMap.openOr(SiteMap()).menus map (menu => menu.loc.link.uriList.mkString("/"))

  val startPageField = select(S ? "START_PAGE", startPage.url.is, urls.sorted)
  val languageField = select[(String, String)](S ? "WEBSITE_LANGUAGE", 
      (setupInfo.language.is, LocaleHelpers.languageName(setupInfo.language.is)), 
      (null, S ? "MULTILINGUAL") +: LocaleHelpers.languages)(LocaleHelpers.languagesPromoter)

  def finish() {
    startPage.url(startPageField)
    startPage.save
    
    setupInfo.language(languageField.is._1)
    setupInfo.save

    S.notice(S ? "SAVED_CHANGES")

    S.redirectTo("/admin/general")
  }

}