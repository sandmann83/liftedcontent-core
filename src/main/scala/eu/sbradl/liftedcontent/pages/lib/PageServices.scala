package eu.sbradl.liftedcontent.pages.lib

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.http.S._

object PageServices extends RestHelper {

  serve {
    case "save" :: _ JsonPut json -> _ => {
      println("saved " + json.toString)
      new OkResponse
    }
  }

}