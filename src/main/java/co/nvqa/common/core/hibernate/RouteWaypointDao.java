package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.RouteWaypoint;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class RouteWaypointDao extends DbBase {

  public RouteWaypointDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public List<RouteWaypoint> getRouteWaypointsByWaypointId(Long waypointId) {
    String query = "FROM RouteWaypoint WHERE waypointId = :waypointId";
    return findAll(session ->
        session.createQuery(query, RouteWaypoint.class)
            .setParameter("waypointId", waypointId));
  }
}