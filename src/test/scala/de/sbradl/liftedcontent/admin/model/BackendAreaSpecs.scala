package de.sbradl.liftedcontent.admin.model

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import de.sbradl.liftedcontent.util.InMemoryTestDatabase

@RunWith(classOf[JUnitRunner])
class BackendAreaSpecs extends SpecificationWithJUnit {

  val longName = "0123456789" * 7
  val longImage = longName * 2
  val longUrl = longImage

  step {
    InMemoryTestDatabase.init(BackendArea)
  }

  "backend area" should {

    "limit length of name" in {
      val area = BackendArea.create
      area.name(longName)

      area.save must throwAn[Exception].like {
        case e => e.getMessage must startWith("""Value too long for column "NAME""")
      }
    }

    "force name to be at least 3 characters" in {
      val area = BackendArea.create
      area.name("")
      
      area.save must throwAn[Exception].like {
        case e => e.getMessage must startWith("""Value too short for column "NAME""")
      }
    }
    
    "limit length of image" in {
      val area = BackendArea.create
      area.image(longImage)
      
      area.save must throwAn[Exception].like {
        case e => e.getMessage must startWith("""Value too long for column "IMAGE""")
      }
    }
    
    "force image to be at least 1 character" in {
      val area = BackendArea.create
      area.image("")
      
      area.save must throwAn[Exception].like {
        case e => e.getMessage must startWith("""Value too short for column "IMAGE""")
      }
    }
    
    "limit length of url" in {
      val area = BackendArea.create
      area.url(longUrl)
      
      area.save must throwAn[Exception].like {
        case e => e.getMessage must startWith("""Value too long for column "URL""")
      }
    }
    
    "force url to be at least 1 character" in {
      val area = BackendArea.create
      area.url("")
      
      area.save must throwAn[Exception].like {
        case e => e.getMessage must startWith("""Value too short for column "URL""")
      }
    }

  }

}