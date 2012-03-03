package eu.sbradl.liftedcontent.pages.snippet

import eu.sbradl.liftedcontent.core.model.User
import eu.sbradl.liftedcontent.pages.model.{Page => PageModel}
import eu.sbradl.liftedcontent.pages.model.PageContent
import net.liftweb.http.LiftScreen
import net.liftweb.http.S
import net.liftweb.util.AnyVar.whatVarIs

class TranslatePage(page: PageModel) extends LiftScreen {

  object content extends ScreenVar(PageContent.create.page(page))
  
  val language = field(content.language)
  val published = field(content.published)
  
  def finish = {
    content.translator(User.currentUser.open_!)
    content.title(S ? ("TITLE", content.language.isAsLocale))
    page.contents += content
    page.save
    
    S.redirectTo(content.url)
  }
  
}