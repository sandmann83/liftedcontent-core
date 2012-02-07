package de.sbradl.liftedcontent.core

import de.sbradl.liftedcontent.util.Module
import de.sbradl.liftedcontent.core.model.User

class UserModule extends Module {
  
  def name = "Users"

  override def mappers = List(User)
  
}