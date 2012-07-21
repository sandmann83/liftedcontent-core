package eu.sbradl.liftedcontent.pages.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S
import net.liftweb.builtin.snippet.Embed
import net.liftweb.http.LiftSession
import scala.xml.MetaData
import scala.xml.Attribute
import scala.xml.Text
import scala.xml.Null
import eu.sbradl.liftedcontent.pages.model.PageContent

class PageTemplateSelector(content: PageContent) {

  def render(children: NodeSeq) = {
    S.withAttrs(metadata)({
      Embed.render(children)
    })
  }
  
  private def metadata: MetaData = Attribute(None, "what", Text(template), Null)  
  
  private def template = content.page.obj.open_!.template.is match {
    case null => "page-1"
    case t => t
  }
    
}