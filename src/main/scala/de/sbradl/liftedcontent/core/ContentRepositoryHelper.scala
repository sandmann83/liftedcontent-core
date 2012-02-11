package de.sbradl.liftedcontent.core

import de.sbradl.liftedcontent.util.Module
import de.sbradl.liftedcontent.admin.model.BackendArea

object ContentRepositoryHelper extends Module {

  def name = "ContentRepositoryHelper"

  override def init {
	  BackendArea.insert("UPLOAD_FILE", "upload.png", "upload-file", "UPLOAD_FILE")
  }

}