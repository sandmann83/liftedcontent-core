package eu.sbradl.liftedcontent.core

import eu.sbradl.liftedcontent.util.Module
import eu.sbradl.liftedcontent.admin.model.BackendArea

object ContentRepositoryHelper extends Module {

  def name = "ContentRepositoryHelper"

  override def init {
	  BackendArea.insert("UPLOAD_FILE", "upload.png", "upload-file", "UPLOAD_FILE")
  }

}