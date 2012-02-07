package de.sbradl.liftedcontent.core.snippet

import de.sbradl.liftedcontent.core.model.Role
import de.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.wizard.Wizard

class SetupWizard extends Wizard {
  
  override def finishButton = <button>{ S ? "FINISH" }</button>
  override def cancelButton = <button>{ S ? "CANCEL" }</button>
  override def nextButton = <button>{S ? "NEXT"}</button>
  override def prevButton = <button>{S ? "PREVIOUS"}</button>
  
  object setupInformation extends WizardVar(de.sbradl.liftedcontent.core.model.SetupInformation.create)
  
  object adminUser extends WizardVar(User.create.superUser(true).validated(true))
  object guestUser extends WizardVar(User.create.firstName("Guest"))

  val generalScreen = new Screen {
    override def screenTop = Full(<h2>{S ? "SITE_INFORMATION"}</h2>)
    addFields(() => setupInformation)
  }
  
  val adminScreen = new Screen {
    override def screenTop = Full(<h2>{S ? "CREATE_ADMIN_USER"}</h2>)
    addFields(() => adminUser)
  }
  
  def finish() {
    setupInformation.save
   
    Role.createDefaultRoles
    adminUser.save
    guestUser.save
    User.logUserIn(adminUser)
    
    S.notice(S ? "SETUP_FINISHED")
    S.redirectTo("/admin/permissions")
  }
  
}
