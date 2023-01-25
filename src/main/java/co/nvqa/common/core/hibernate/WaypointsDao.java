package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.JobWaypoint;
import co.nvqa.common.core.model.persisted_class.Waypoints;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class WaypointsDao extends DbBase {

  public WaypointsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class");
  }

  public List <Waypoints> getWaypointsDetailsByWaypointId(Long wayPointId) {
    List<Waypoints> results;
    String query = "FROM Waypoints "
        + "WHERE id = :waypointId";
    results = findAll(session ->
        session.createQuery(query, Waypoints.class)
            .setParameter("waypointId", wayPointId));
    return results;
  }
}