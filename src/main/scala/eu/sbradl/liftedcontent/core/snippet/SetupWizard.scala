package eu.sbradl.liftedcontent.core.snippet

import eu.sbradl.liftedcontent.core.model.Role
import eu.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.wizard.Wizard

class SetupWizard extends Wizard {
  
  override def finishButton = <button>{ S ? "FINISH" }</button>
  override def cancelButton = <button>{ S ? "CANCEL" }</button>
  override def nextButton = <button>{S ? "NEXT"}</button>
  override def prevButton = <button>{S ? "PREVIOUS"}</button>
  
  object adminUser extends WizardVar(User.create.superUser(true).validated(true))
  object guestUser extends WizardVar(User.create.firstName("Guest"))

  val adminScreen = new Screen {
    override def screenTop = Full(<h2>{S ? "CREATE_ADMIN_USER"}</h2>)
    addFields(() => adminUser)
  }
  
  def finish() {
    Role.createDefaultRoles
    adminUser.save
    guestUser.save
    User.logUserIn(adminUser)
    
    S.notice(S ? "SETUP_FINISHED")
    S.redirectTo("/admin/permissions")
  }
  
}
