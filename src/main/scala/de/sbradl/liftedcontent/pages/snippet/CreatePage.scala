package de.sbradl.liftedcontent.pages.snippet

import de.sbradl.liftedcontent.core.model.User
import de.sbradl.liftedcontent.pages.model.{Page => PageModel}
import net.liftweb.http.LiftScreen
import net.liftweb.util.AnyVar.whatVarIs

class CreatePage extends LiftScreen {

  object page extends ScreenVar(PageModel.create)
  
  val name = field(page.name)
  
  def finish {
    page.author(User.currentUser.open_!)
    
    page.save
  }
  
}