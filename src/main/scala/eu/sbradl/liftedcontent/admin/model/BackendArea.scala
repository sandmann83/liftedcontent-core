package eu.sbradl.liftedcontent.admin.model

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._
import Loc._
import net.liftweb.mapper.LongKeyedMetaMapper
import net.liftweb.mapper.LongKeyedMapper
import net.liftweb.mapper.IdPK
import net.liftweb.mapper.MappedString
import net.liftweb.http.S
import net.liftweb.mapper.MappedTextarea
import net.liftweb.mapper.BaseMetaMapper
import net.liftweb.mapper.By
import net.liftweb.sitemap.Menu.Menuable

object BackendArea extends BackendArea with LongKeyedMetaMapper[BackendArea] {

  def insert(name: String, image: String, url: String, description: String) {
    val result = BackendArea.find(By(BackendArea.name, name))

    if (result.isEmpty) {
      val area = BackendArea.create
      area.name(name)
      area.image(image)
      area.url(url)
      area.description(description)
      area.save
    }
  }

  def addMenu(parent: String, menu: List[ConvertableToMenu]) {
    otherMenus += ((parent, menu))
  }

  def menus: List[ConvertableToMenu] = (findAll() map {
    area =>
      {
        Menu.i(area.name.is) / "admin" / area.url.is submenus (
          otherMenus.getOrElse(area.url.is, Nil))
      }
  })

  private var otherMenus: Map[String, List[ConvertableToMenu]] = Map()

}

class BackendArea extends LongKeyedMapper[BackendArea] with IdPK {

  def getSingleton = BackendArea

  object name extends MappedString(this, 64) {
    override def displayName = S ? "NAME"
    override def validations = List(
      valMinLen(3, S ?? "NAME_TOO_SHORT") _,
      valMaxLen(64, S ?? "NAME_TOO_LONG") _)
  }

  object image extends MappedString(this, 128) {
    override def displayName = S ? "IMAGE_NAME"
    override def validations = List(
      valMinLen(1, S ?? "NAME_TOO_SHORT") _,
      valMaxLen(128, S ?? "NAME_TOO_LONG") _)
  }

  object url extends MappedString(this, 128) {
    override def displayName = S ? "URL"
    override def validations = List(
      valMinLen(1, S ?? "NAME_TOO_SHORT") _,
      valMaxLen(128, S ?? "NAME_TOO_LONG") _)
  }

  object description extends MappedTextarea(this, 512) {
    override def displayName = S ? "DESCRIPTION"
  }
}