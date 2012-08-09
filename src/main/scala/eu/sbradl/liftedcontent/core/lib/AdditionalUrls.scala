package eu.sbradl.liftedcontent.core.lib

import scala.collection.mutable.ListBuffer

object AdditionalUrls {

  val urlCalculators: ListBuffer[() => List[String]] = ListBuffer()
  
  def apply(): List[String] = urlCalculators.flatMap(_()).toList
  
  
}