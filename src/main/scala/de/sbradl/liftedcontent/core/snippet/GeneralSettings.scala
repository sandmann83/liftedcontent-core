package de.sbradl.liftedcontent.core.snippet

import de.sbradl.liftedcontent.core.model.StartPage
import net.liftweb.http.LiftRulesMocker.toLiftRules
import net.liftweb.http.LiftRules
import net.liftweb.http.LiftScreen
import net.liftweb.http.S
import net.liftweb.sitemap.SiteMap
import de.sbradl.liftedcontent.core.model.MobileStartPage

class GeneralSettings extends LiftScreen {

  override def finishButton = <button>{ S ? "SAVE" }</button>
  override def cancelButton = <button>{ S ? "CANCEL" }</button>

  object setupInformation extends ScreenVar(de.sbradl.liftedcontent.core.model.SetupInformation.find().openOr(de.sbradl.liftedcontent.core.model.SetupInformation.create))
  object startPage extends ScreenVar(StartPage.find().openOr(StartPage.create))
  object mobileStartPage extends ScreenVar(MobileStartPage.find().openOr(MobileStartPage.create))

  addFields(() => setupInformation)

  val urls = LiftRules.siteMap.openOr(SiteMap()).menus map (menu => menu.loc.link.uriList.mkString("/"))

  val startPageField = select(
    S ? "START_PAGE",
    startPage.url.is,
    urls.sortWith((e1, e2) => (e1 compareTo e2) < 0))

  val mobileStartPageField = select(
    S ? "MOBILE_START_PAGE",
    mobileStartPage.url.is,
    urls.sorted)

  def finish() {
    setupInformation.save
    
    startPage.url(startPageField)
    startPage.save
    
    mobileStartPage.url(mobileStartPageField)
    mobileStartPage.save

    S.notice(S ? "SAVED_CHANGES")

    S.redirectTo(S.uri)
  }

}