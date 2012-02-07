package de.sbradl.liftedcontent.core.model

import net.liftweb.mapper._
import net.liftweb.http.S

object Role extends Role with LongKeyedMetaMapper[Role] {

  val adminRoleID = 1
  val guestRoleID = 2
  val memberRoleID = 3

  def adminRole = find(By(id, adminRoleID)).open_!
  def memberRole = find(By(id, memberRoleID)).open_!

  override def dbTableName = "roles"
  override def dbIndexes = UniqueIndex(name) :: super.dbIndexes

  def isDefaultRole(role: Role) = List(adminRoleID, guestRoleID, memberRoleID) contains role.id.is

  def createDefaultRoles {
    createIfNotExists("Admin")
    createIfNotExists("Guest")
    createIfNotExists("Member")
  }

  private def createIfNotExists(name: String) = groupExists(name) match {
    case true =>
    case false => {
      val role = Role.create
      role.name(name)
      role.save
    }
  }

  private def groupExists(name: String) = {
    Role.count(By(Role.name, name)) > 0
  }

}

class Role extends LongKeyedMapper[Role] with IdPK with ManyToMany {
  def getSingleton = Role

  object name extends MappedString(this, 32) {
    override def displayName = S ? "NAME"

    override def validations = List(
      valUnique(S ? "ROLE_ALREADY_EXISTS"),
      valMinLen(3, S ? "ROLE_NAME_TOO_SHORT"))

    override def setFilter = trim _ :: notNull _ :: super.setFilter
  }

  object users extends MappedManyToMany(UserRoles, UserRoles.role, UserRoles.user, User)
}