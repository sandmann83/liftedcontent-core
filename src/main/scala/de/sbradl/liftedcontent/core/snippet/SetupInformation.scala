package de.sbradl.liftedcontent.core.snippet

import scala.xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.common.Empty
import net.liftweb.common.Full

class SetupInformation {
  
  def render = {
    "data-lift-id=title" #> name &
    "data-lift-id=subtitle" #> subtitle
  }
  
  private def name: String = information match {
    case Empty => "LiftedContent"
    case Full(info) => info.name
  }
  
  private def subtitle: String = information match {
    case Empty => "Scala/Lift based CMS"
    case Full(info) => info.subtitle
  }
  
  private def information = {
    val data = de.sbradl.liftedcontent.core.model.SetupInformation.findAll()
    
    data match {
      case List() => Empty
      case List(info) => Full(info)
    }
  }
}