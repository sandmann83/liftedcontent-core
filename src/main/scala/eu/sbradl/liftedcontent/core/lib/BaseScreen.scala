package eu.sbradl.liftedcontent.core.lib

import net.liftweb.http.CssBoundLiftScreen
import scala.xml.NodeSeq
import net.liftweb.http.js.JsCmds.SetHtml
import net.liftweb.http._
import net.liftweb.common.Full
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds

trait BaseScreen extends CssBoundLiftScreen {

  override def defaultToAjax_? : Boolean = true

  //override def allTemplate = defaultAllTemplate

  protected override lazy val cssClassBinding = new CssClassBinding {
    override def label = "control-label"
  }

  override def validate = {
    val errors = super.validate

    for (error <- errors) {
      error.field.uniqueFieldId match {
        case Full(fieldName) => S.appendJs(JsCmds.Run("$('[for=\"" + fieldName + "\"]').parent().addClass('error')"))
        case _ =>
      }

    }

    errors
  }

  protected def fileUpload(labelText: String) = new Field {
    override def uploadField_? = true

    type ValueType = FileParamHolder

    def manifest = scala.Predef.manifest[FileParamHolder]

    def name = labelText

    def default = FileParamHolder("", "", "", new Array[Byte](0))

    override def toForm = Full(SHtml.fileUpload(set _))
  }

}