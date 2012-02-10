package de.sbradl.liftedcontent.core.lib

import de.sbradl.liftedcontent.util.Module
import de.sbradl.liftedcontent.core.SetupModule
import net.liftweb.http.LiftRules
import net.liftweb.sitemap.ConvertableToMenu

object ModuleManager {

  private var modules = List[Module]()
  
  def registeredModules: List[Module] = modules

  def register(module: Module) {
    modules ::= module

    module.mappers foreach {
      Database.addMapper(_)
    }
    
    addToPackages
  }

  def init {
    LiftRules.resourceNames ++= modules flatMap (_.resourceNames)
    
    modules foreach (_.init)
  }
  
  def menus = modules flatMap(_.menus)
  
  private def addToPackages = modules foreach (m => LiftRules.addToPackages(m.packageToAdd))

}