package de.sbradl.liftedcontent.admin

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import de.sbradl.liftedcontent.util.InMemoryTestDatabase
import model.BackendArea

@RunWith(classOf[JUnitRunner])
class AdminModuleSpecs extends SpecificationWithJUnit {
  
  val module = new AdminModule
  InMemoryTestDatabase.init(BackendArea)
  
  "admin module" should {
    
    "have exactly one top level menu" in {
      module.menus must have size 1
    }
    
    "insert submenu for each backend area in the database" in {
      BackendArea.insert("submenu1", "", "", "")
      BackendArea.insert("submenu2", "", "", "")
      BackendArea.insert("submenu3", "", "", "")
      
      module.menus.head.submenus must have size 3
    }
    
//    "use test function at top level menu" in {
//      val module1 = new AdminModule {
//        override def menuTest = false
//      }
//      
//      val module2 = new AdminModule {
//        override def menuTest = true
//      }
//      
//      failure("not implemented")
//    }

  }
}