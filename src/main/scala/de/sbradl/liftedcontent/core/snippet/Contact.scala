package de.sbradl.liftedcontent.core.snippet

import net.liftweb.http.LiftScreen
import net.liftweb.http.S
import de.sbradl.liftedcontent.core.model.User
import net.liftweb.common.Full
import net.liftweb.util.Mailer
import net.liftweb.util.Mailer.From
import net.liftweb.util.Mailer.Subject
import net.liftweb.util.Mailer.PlainMailBodyType
import net.liftweb.util.Mailer.To
import net.liftweb.util.Mailer.ReplyTo

class Contact extends LiftScreen {

  object user extends ScreenVar(User.currentUser)

  val name = field(S ? "NAME", "")
  val email = field(S ? "EMAIL", "")
  val subject = field(S ? "SUBJECT", "")
  val message = textarea(S ? "MESSAGE", "")

  def finish {
    sendMail(name, email, subject, message)
    
    S.notice(S ? "CONTACT_REQUEST_SENT")
  }

  private def sendMail(name: String, email: String, subject: String,
    message: String) {
    Mailer.sendMail(From("contact@" + S.hostName ), Subject(subject),
      (PlainMailBodyType(message) :: To("NOT_IMPLEMENTED") :: ReplyTo(email, Full(name)) :: Nil): _*)
  }

}