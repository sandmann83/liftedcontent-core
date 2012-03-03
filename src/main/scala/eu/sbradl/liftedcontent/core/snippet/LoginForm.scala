package eu.sbradl.liftedcontent.core.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.S

class LoginForm {

  def render = {
    val emailName = nextFuncName
    val passwordName = nextFuncName

    "data-lift-id=username [name]" #> emailName &
      "data-lift-id=username [placeholder]" #> (S ? "EMAIL") &
      "data-lift-id=username [type]" #> "email" &
      "data-lift-id=password [name]" #> passwordName &
      "data-lift-id=password [placeholder]" #> (S ? "PASSWORD") &
      "data-lift-id=password [type]" #> "password"
  }
}