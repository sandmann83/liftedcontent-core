package eu.sbradl.liftedcontent.core.lib

import net.liftweb.mapper.DB
import net.liftweb.db.StandardDBVendor
import net.liftweb.util.Props
import net.liftweb.http.LiftRules
import net.liftweb.db.DefaultConnectionIdentifier
import net.liftweb.mapper.Schemifier
import net.liftweb.mapper.BaseMetaMapper
import net.liftweb.http.S
import net.liftweb.db.DBLogEntry
import net.liftweb.common.Logger

object Database extends Logger {

  private var mappers = List[BaseMetaMapper]()

  def addMapper(mapper: BaseMetaMapper) {
    mappers ::= mapper
  }

  def init {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    Schemifier.schemify(true, Schemifier.infoF _, mappers: _*)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)

    if (Props.mode == Props.RunModes.Development) {
      DB.addLogFunc {
        case (query, time) => {
          info("All queries took " + time + "ms")
          query.allEntries.foreach({
            case DBLogEntry(stmt, duration) =>
              info(stmt + " took " + duration + "ms")
          })
        }
      }
    }
  }

}