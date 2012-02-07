package de.sbradl.liftedcontent.core.snippet

import de.sbradl.liftedcontent.core.model.User
import scala.xml.NodeSeq

class TestCondition {

  def isAdmin(in: NodeSeq) = User.superUser_? match {
    case true => in
    case false => NodeSeq.Empty
  }
  
}