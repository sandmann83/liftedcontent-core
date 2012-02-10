package eu.sbradl.liftedcontent.core.model

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import de.sbradl.liftedcontent.util.InMemoryTestDatabase
import de.sbradl.liftedcontent.core.model.User
import de.sbradl.liftedcontent.core.model.UserRoles
import de.sbradl.liftedcontent.core.model.Role
import net.liftweb.mapper.By
import net.liftweb.common.Full

@RunWith(classOf[JUnitRunner])
class UserManagementSpecs extends SpecificationWithJUnit {
  
  
  
  step {
    InMemoryTestDatabase.init(User, UserRoles, Role)
    Role.createDefaultRoles
  }
  
  "Saving a user" should {
    
    "add him to the member role if neccessary" in {
      User.create.firstName("TestUser").save
      
      val user = User.find(By(User.firstName, "TestUser")).open_!
      val role = Role.memberRole
      
      UserRoles.count(By(UserRoles.user, user), By(UserRoles.role, role)) must be equalTo 1
    }

  }
  
  "Deleting a user" should {
    "also remove his memberships" in {
      val user = User.find(By(User.firstName, "TestUser"))
      user must beLike {case Full(_) => ok}
      
      user.open_!.delete_!
      val role = Role.memberRole
      
      UserRoles.count(By(UserRoles.user, user), By(UserRoles.role, role)) must be equalTo 0
    }
  }
}