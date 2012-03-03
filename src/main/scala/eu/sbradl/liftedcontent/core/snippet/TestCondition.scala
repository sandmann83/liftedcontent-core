package eu.sbradl.liftedcontent.core.snippet

import eu.sbradl.liftedcontent.core.model.User
import scala.xml.NodeSeq

class TestCondition {

  def isAdmin(in: NodeSeq) = User.superUser_? match {
    case true => in
    case false => NodeSeq.Empty
  }
  
  def notAdmin(in: NodeSeq) = User.superUser_? match {
    case false => in
    case true => NodeSeq.Empty
  }
  
}