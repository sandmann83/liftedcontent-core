package eu.sbradl.liftedcontent.core.snippet

import net.liftweb.http.LiftScreen
import eu.sbradl.liftedcontent.core.model.SiteMetaData
import eu.sbradl.liftedcontent.core.lib.RequestHelpers
import net.liftweb.http.S

class AddMetaTag extends LiftScreen {

  object metaTag extends ScreenVar(SiteMetaData.create)
  object urls extends ScreenVar(RequestHelpers.allPaths)
 
  val url = select(S ? "URL", urls.head, urls)
  val name = select(S ? "NAME", "keywords", Seq("application-name", "author", "description", "generator", "keywords"))
  addFields(() => metaTag.content)
  
  def finish {
    metaTag.url(url)
    metaTag.name(name)
    metaTag.save
    
    S.notice(S ? "SAVED_CHANGES")
  }
  
}