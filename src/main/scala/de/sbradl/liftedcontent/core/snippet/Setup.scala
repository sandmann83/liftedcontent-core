package de.sbradl.liftedcontent.core.snippet

import net.liftweb.util.Helpers._
import de.sbradl.liftedcontent.core.lib.ModuleManager
import net.liftweb.http.S

class Setup {

  def render = {
    val modules = ModuleManager.registeredModules filter (_.needsSetup) filterNot (_.isInstalled)

    "data-lift-id=module" #> modules.map {
      module =>
        {
          "data-lift-id=url [href]" #> ("/setup/" + module.id) &
            "data-lift-id=name *" #> (S ? module.name) &
            "data-lift-id=icon [src]" #> ("/images/backend/" + module.id + ".png")
        }
    }
  }

}