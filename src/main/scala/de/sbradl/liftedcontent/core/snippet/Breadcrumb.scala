package de.sbradl.liftedcontent.core.snippet

import net.liftweb.util._
import net.liftweb.common._
import Helpers._
import net.liftweb.sitemap.Loc
import net.liftweb.http.S
import scala.xml.Text

class Breadcrumb {

  def render = "*" #> {
    val breadcrumbs: List[Loc[_]] =
      for {
        currentLoc <- S.location.toList
        loc <- currentLoc.breadCrumbs
      } yield loc
    "li *" #> breadcrumbs.map {
      loc =>
        ".link *" #> loc.title &
          ".link [href]" #> loc.createDefaultLink.getOrElse(Text("#"))
    }
  }

}