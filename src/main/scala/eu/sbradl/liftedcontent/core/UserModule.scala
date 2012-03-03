package eu.sbradl.liftedcontent.core

import eu.sbradl.liftedcontent.util.Module
import eu.sbradl.liftedcontent.core.model.User

class UserModule extends Module {
  
  def name = "Users"

  override def mappers = List(User)
  
}