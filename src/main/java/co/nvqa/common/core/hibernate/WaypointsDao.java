package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.Waypoints;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class WaypointsDao extends DbBase {

  public WaypointsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public List<Waypoints> getWaypointsDetailsByWaypointId(Long wayPointId) {
    List<Waypoints> results;
    String query = "FROM Waypoints WHERE id = :waypointId";
    results = findAll(session ->
        session.createQuery(query, Waypoints.class)
            .setParameter("waypointId", wayPointId));
    return results;
  }

  public List<Waypoints> getWaypointsDetailsByRouteId(Long routeId) {
    List<Waypoints> results;
    String query = "FROM Waypoints WHERE routeId = :routeId ORDER BY seqNo";
    results = findAll(session ->
        session.createQuery(query, Waypoints.class)
            .setParameter("routeId", routeId));
    return results;
  }

  public Waypoints getWaypointsDetails(Long wayPointId) {
    String query = "FROM Waypoints WHERE id = :waypointId";
    var result = findAll(session ->
        session.createQuery(query, Waypoints.class)
            .setParameter("waypointId", wayPointId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }
}