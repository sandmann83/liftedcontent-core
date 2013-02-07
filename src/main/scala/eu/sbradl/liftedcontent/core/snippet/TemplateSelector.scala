package eu.sbradl.liftedcontent.core.snippet

import scala.xml.Attribute
import scala.xml.MetaData
import scala.xml.NodeSeq
import scala.xml.Null
import scala.xml.Text
import net.liftweb.builtin.snippet.Surround
import net.liftweb.http.S
import net.liftweb.http.LiftRules
import eu.sbradl.liftedcontent.core.lib.UserAgentDetector
import eu.sbradl.liftedcontent.core.lib.RequestHelpers
import net.liftweb.common.Full

object TemplateSelector extends TemplateSelector {
  def apply() = new TemplateSelector
}

class TemplateSelector {

  def render(children: NodeSeq) = {
    S.withAttrs(metadata)({
      Surround.render(children)
    })
  }

  def template = UserAgentDetector.isMobile match {
    case true => "mobile"
    case false => "default"
  }

  def content = "content"

  private def metadata: MetaData = Attribute(None, "at", Text(content),
    Attribute(None, "with", Text(template), Null))

}