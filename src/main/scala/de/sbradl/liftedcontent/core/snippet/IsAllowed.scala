package de.sbradl.liftedcontent.core.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S
import net.liftweb.common.Full
import de.sbradl.liftedcontent.core.lib.ACL

class IsAllowed {

  def render(in: NodeSeq) = S.attr("url") match {
    case Full(url) if ACL.isAllowed(url) => in
    case _ => NodeSeq.Empty
  }
  
}