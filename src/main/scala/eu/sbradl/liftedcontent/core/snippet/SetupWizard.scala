package eu.sbradl.liftedcontent.core.snippet

import eu.sbradl.liftedcontent.core.model.Role
import eu.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.wizard.Wizard
import eu.sbradl.liftedcontent.core.lib.BaseScreen

class SetupWizard extends BaseScreen {
  
  def formName = "wizardAll"
    
  override def screenTitle = <h2>{S ? "CREATE_ADMIN_USER"}</h2>
  
  object adminUser extends ScreenVar(User.create.superUser(true).validated(true))
  object guestUser extends ScreenVar(User.create.firstName("Guest"))

  addFields(() => adminUser.firstName)
  addFields(() => adminUser.lastName)
  addFields(() => adminUser.email)
  addFields(() => adminUser.locale)
  addFields(() => adminUser.timezone)
  addFields(() => adminUser.password)
  
  def finish {
    Role.createDefaultRoles
    
    adminUser.save
    guestUser.save
    
    User.logUserIn(adminUser)
    
    S.notice(S ? "SETUP_FINISHED")
    S.redirectTo("/admin/permissions")
  }
  
}
