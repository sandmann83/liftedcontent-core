package eu.sbradl.liftedcontent.core.lib

import net.liftweb.util.Props
import net.liftweb.util.Mailer
import net.liftweb.common.Full
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication

object MailConfigurator {

  def init {
    var isAuth = Props.get("mail.smtp.auth", "false").toBoolean

    Mailer.customProperties = Map(
        "mail.smtp.host" -> Props.get("mail.smtp.host", "localhost"),
        "mail.smtp.port" -> Props.get("mail.smtp.port", "25"),
        "mail.smtp.auth" -> Props.get("mail.smtp.auth", "false")
    )

    if (isAuth) {
      (Props.get("mail.user"), Props.get("mail.password")) match {
        case (Full(username), Full(password)) =>
          Mailer.authenticator = Full(new Authenticator() {
            override def getPasswordAuthentication = new PasswordAuthentication(username, password)
          })
        case _ => new Exception("Username/password not supplied for Mailer.")
      }
    }
  }

}