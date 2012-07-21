package eu.sbradl.liftedcontent.pages.lib

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import eu.sbradl.liftedcontent.pages.model.PageContent
import net.liftweb.common.Full
import eu.sbradl.liftedcontent.pages.model.PageRegion

object PageServices extends RestHelper {

  serve {
    case "save" :: _ JsonPut json -> _ => save(json)
  }

  private def save(input: JValue) = {
    val regions = input \ "content" children

    val head = extractData(regions.head)

    val id = head._1
    val title = head._2

    PageContent.find(id) match {
      case Full(page) => {
        page.title(title)

        regions.tail foreach {
          r =>
            {
              val (key, value) = extractData(r)

              page.regions.find(_.name.is == key) match {
                case Some(region) => region.text(value)
                case _ => {
                  val region = PageRegion.create.belongsTo(page).name(key).text(value)
                  region.save
                  page.regions += region
                }
              }
            }
        }

        page.save

        S.notice("SAVED_CHANGES")

        new OkResponse
      }
      case _ => PlainTextResponse("invalid page id", 500)
    }
  }

  private def extractData(data: JValue) = {
    val field = data.asInstanceOf[JField]

    (field.name, (field \ "value").asInstanceOf[JString].values)
  }
}