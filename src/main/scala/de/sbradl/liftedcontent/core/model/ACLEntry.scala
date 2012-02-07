package de.sbradl.liftedcontent.core.model

import net.liftweb.mapper._
import net.liftweb.http.S

object ACLEntry extends ACLEntry with LongKeyedMetaMapper[ACLEntry] {

  override def dbTableName = "acl"

}

class ACLEntry extends LongKeyedMapper[ACLEntry] with IdPK {
  
  def getSingleton = ACLEntry

  object role extends MappedLongForeignKey(this, Role)
  object url extends MappedString(this, 128)
  
}