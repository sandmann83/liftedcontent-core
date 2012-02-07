package de.sbradl.liftedcontent.admin.snippet

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import de.sbradl.liftedcontent.util.InMemoryTestDatabase
import net.liftweb.http.LiftSession
import net.liftweb.util.StringHelpers
import net.liftweb.common.Empty
import net.liftweb.http.S
import scala.xml.Text
import scala.xml.Node
import scala.xml.NodeSeq

@RunWith(classOf[JUnitRunner])
class BackendAreaSnippetSpecs extends SpecificationWithJUnit {

  val session = new LiftSession("", StringHelpers.randomString(20), Empty)
  val snippet = new ListBackendAreas

  step {
    InMemoryTestDatabase.init(de.sbradl.liftedcontent.admin.model.BackendArea)
    de.sbradl.liftedcontent.admin.model.BackendArea.insert("areaname", "areaimage", "areaurl", "areadescription")
  }

  "backend area snippet" should {
    "put the name in the node with data-name=name" in {
      S.initIfUninitted(session) {
        val result: NodeSeq = snippet.render(<div data-name="area">
                                               <span data-name="name"></span>
                                             </div>)

        (result \\ "span").text mustEqual "areaname"
      }
    }
    
    "put the description in the node with data-name=description" in {
      S.initIfUninitted(session) {
        val result: NodeSeq = snippet.render(<div data-name="area">
                                               <span data-name="description"></span>
                                             </div>)

        (result \\ "span").text mustEqual "areadescription"
      }
    }
    
    "put the url at the href attribute in the node with data-name=url" in {
      S.initIfUninitted(session) {
        val result: NodeSeq = snippet.render(<div data-name="area">
                                               <a data-name="url"></a>
                                             </div>)

        (result \\ "a" \ "@href").text mustEqual "/admin/areaurl"
      }
    }
    
    "put the description at the title attribute in the node with data-name=url" in {
      S.initIfUninitted(session) {
        val result: NodeSeq = snippet.render(<div data-name="area">
                                               <a data-name="url"></a>
                                             </div>)

        (result \\ "a" \ "@title").text mustEqual "areadescription"
      }
    }
    
    "put the image location at the src attribute in the node with data-name=icon" in {
      S.initIfUninitted(session) {
        val result: NodeSeq = snippet.render(<div data-name="area">
                                               <img data-name="icon" />
                                             </div>)

        (result \\ "img" \ "@src").text mustEqual "/images/backend/areaimage"
      }
    }
  }
  
}