package eu.sbradl.liftedcontent.core.model

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http.S
import net.liftweb.sitemap.Loc.LocGroup
import eu.sbradl.liftedcontent.core.SetupModule
import net.liftweb.sitemap.Loc.If
import eu.sbradl.liftedcontent.core.snippet.TemplateSelector

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with={ TemplateSelector().template } at={ TemplateSelector().content }>
                                   <lift:bind/>
                                 </lift:surround>)

  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
    locale, timezone, password)

  // comment this line out to require email validations
//  override def skipEmailValidation = true

  override def globalUserLocParams = List(LocGroup("user"))

  override def loginXhtml = <article>
                              <header>
                                <h1>{ S ? "LOGIN" }</h1>
                              </header>
                              <form method="post" action={ S.uri }>
                                <label for="username" class="ui-hidden-accessible">{ S ? "EMAIL" }</label>
                                <user:email></user:email>
                                <label for="password" class="ui-hidden-accessible">{ S ? "PASSWORD" }</label>
                                <user:password></user:password>
                                <user:submit></user:submit>
                              </form>
                            </article>

  override def signupXhtml(user: User) = <article>
                                           <header>
                                             <h1>{ S ? "REGISTER" }</h1>
                                           </header>
                                           <form method="post" action={ S.uri }>
                                             <table>
                                               { localForm(user, false, signupFields) }
                                               <tr><td colspan="2"><user:submit></user:submit></td></tr>
                                             </table>
                                           </form>
                                         </article>

  override def lostPasswordXhtml = <article>
                                     <header>
                                       <h1>{ S ? "RECOVER_PASSWORD" }</h1>
                                       <p>{ S.??("enter.email") }</p>
                                     </header>
                                     <form method="post" action={ S.uri }>
                                       <table>
                                         <tr><td>{ userNameFieldString }</td><td><user:email/></td></tr>
                                         <tr><td>&nbsp;</td><td><user:submit/></td></tr>
                                       </table>
                                     </form>
                                   </article>

  def effectiveRoles = currentUser match {
    case Full(user) => user.roles
    case _ => Role.find(By(Role.id, Role.guestRoleID)) match {
      case Full(role) => List(role)
      case _ => List()
    }
  }

  def guestUser = User.find(By(User.firstName, "Guest")).open_!

  override def beforeSave = addGroupIfNeeded _ :: super.beforeSave
  
  override def afterDelete = deleteMemberships _ :: super.afterDelete
}

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class User extends MegaProtoUser[User] with ManyToMany {
  def getSingleton = User

  def name = firstName.is match {
    case "Guest" => S ? "GUEST"
    case _ => firstName + " " + lastName
  }

  object roles extends MappedManyToMany(UserRoles, UserRoles.user, UserRoles.role, Role)

  def addGroupIfNeeded(user: User) {
    if (superUser.is) {
      if (!isMemberOf(Role.adminRoleID)) {
        user.roles += Role.adminRole
      }
    } else {
      if (!isMemberOf(Role.memberRoleID)) {
        user.roles += Role.memberRole
      }
    }
  }
  
  def deleteMemberships(user: User) {
    UserRoles.findAll(By(UserRoles.user, user)) foreach {
      _.delete_!
    }
  }

  private def isMemberOf(roleId: Long) = roles.contains((role: Role) => role.id.is == roleId)

}
