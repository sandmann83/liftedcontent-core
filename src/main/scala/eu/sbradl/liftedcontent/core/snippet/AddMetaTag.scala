package eu.sbradl.liftedcontent.core.snippet

import eu.sbradl.liftedcontent.core.model.SiteMetaData
import eu.sbradl.liftedcontent.core.lib.RequestHelpers
import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.util.ValueCell
import net.liftweb.common.Box
import net.liftweb.common.Full
import net.liftweb.mapper.By
import net.liftweb.http.js.JsCmds
import net.liftweb.http.WiringUI
import net.liftweb.http.js.jquery.JqWiringSupport
import net.liftweb.common.Empty

class AddMetaTag {
// TODO: sitemetadata when url = "" -> same as index
  val allUrls = RequestHelpers.allPaths(withAdditionalPaths = true)
  val allNames = List("application-name", "author", "description", "generator", "keywords", "google-site-verification")

  private val selectedUrl = ValueCell[Int](0)
  private val selectedName = ValueCell[String]("keywords")
  
  private val content = selectedName.lift(name => {
    SiteMetaData.find(By(SiteMetaData.url, allUrls(selectedUrl.get)), By(SiteMetaData.name, name)).map(_.content.get)
  })

  def render = {
    var selectedContent = ""
    def process() = {
      try {
        SiteMetaData.find(By(SiteMetaData.url, allUrls(selectedUrl.get)), By(SiteMetaData.name, selectedName.get)) match {
          case Full(metadata) => {
            metadata.content(selectedContent)
            metadata.save
            
            S.notice(S ? "META_DATA_UPDATED")
          }
          case _ => {
            val metadata = SiteMetaData.create
            metadata.url(allUrls(selectedUrl.get))
            metadata.name(selectedName.get)
            metadata.content(selectedContent)
            metadata.save
            
            S.notice(S ? "META_DATA_SAVED")
          }
        }
      } catch {
        case e: Exception => S.error(e.getMessage())
      }
    }

    "data-lift-id=url" #> urlSelect &
      "data-lift-id=name" #> nameSelect &
      "data-lift-id=content" #> WiringUI.toNode(content, JqWiringSupport.fade) {
        (contents, ns) =>
          {
            SHtml.text(content.get.openOr(""), selectedContent = _)
          }
      } &
      "type=submit" #> SHtml.ajaxSubmit(S ? "UPDATE_META_DATA", process, ("class" -> "btn btn-primary"))
  }

  private def urlSelect = {
    val urls = allUrls.map(url => (allUrls.indexOf(url).toString, url))

    SHtml.ajaxSelect(urls, Full(selectedUrl.get.toString), url => { selectedUrl.set(url.toInt); selectedName.set(selectedName.get); JsCmds.Noop })
  }

  private def nameSelect = {
    val names = allNames.map(name => (name, name))

    SHtml.ajaxSelect(names, Full(selectedName.get), name => { selectedName.set(name); JsCmds.Noop })
  }

}