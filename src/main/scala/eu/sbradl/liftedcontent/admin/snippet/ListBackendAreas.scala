package eu.sbradl.liftedcontent.admin.snippet

import scala.xml.NodeSeq
import net.liftweb.util._
import net.liftweb.common._
import Helpers._
import net.liftweb.sitemap.Loc
import net.liftweb.http.S
import scala.xml.Text

class ListBackendAreas {

  def render = {
    val areas = eu.sbradl.liftedcontent.admin.model.BackendArea.findAll()

    "data-lift-id=area" #> areas.map {
      area =>
        {
          "data-lift-id=url [href]" #> ("/admin/" + area.url.is) &
          "data-lift-id=url [title]" #> (S ? area.description.is) &
          "data-lift-id=name *" #> (S ? area.name.is) &
            "data-lift-id=icon [src]" #> ("/images/backend/" + area.image.is) &
            "data-lift-id=description *" #> (S ? area.description.is)
        }
    }
  }

}