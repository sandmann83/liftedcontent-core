package de.sbradl.liftedcontent.pages.snippet

import net.liftweb.util.Helpers._ 
import de.sbradl.liftedcontent.pages.model.{Page => PageModel}

class PagesMenu {

  def render = {
    "*" #> <ul>
             {
               PageModel.findAllByCurrentLocale map {
                 p =>
                   {
                     <li><a href={"/page/" + urlEncode(p.title)}>{p.title}</a></li>
                   }
               }
             }
           </ul>
  }

}