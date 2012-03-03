package eu.sbradl.liftedcontent.core.snippet

import eu.sbradl.liftedcontent.core.lib.RolesChanged
import eu.sbradl.liftedcontent.core.lib.RolesServer
import eu.sbradl.liftedcontent.core.model.Role

import net.liftweb.http.js.JsCmd.unitToJsCmd
import net.liftweb.http.js.JsCmd
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.util.Helpers.strToCssBindPromoter

class AddRole {

  def render = {
    var name = ""

    def process(): JsCmd = {
      val role = Role.create
      role.name(name)
      
      role.validate match {
        case List() => {
          role.save
          S.notice(S ? "ROLE_ADDED")
          
          RolesServer ! RolesChanged
        }
        case ls => ls foreach {
          message => S.error(message.msg)
        }
      }

    }

    "name=name" #> SHtml.text(name, name = _) &
    "name=submit" #> SHtml.ajaxSubmit(S ? "ADD", process)
  }

}