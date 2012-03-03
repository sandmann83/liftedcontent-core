package eu.sbradl.liftedcontent.core.snippet

import net.liftweb.http.S
import eu.sbradl.liftedcontent.core.model.Role
import eu.sbradl.liftedcontent.core.comet.RolesServer
import net.liftweb.http.js.JsCmds
import eu.sbradl.liftedcontent.core.comet.RolesChanged
import net.liftweb.http.js.JsCmd
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml

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