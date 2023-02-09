package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.core.RouteMonitoringData;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class RouteMonitoringDataDao extends DbBase {

  public RouteMonitoringDataDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.core");
  }

  public RouteMonitoringData getRouteMonitoringDataByWaypointId(Long waypointId) {
    String query = "FROM RouteMonitoringData WHERE waypointId = :waypointId";
    List<RouteMonitoringData> result = findAll(session ->
        session.createQuery(query, RouteMonitoringData.class)
            .setParameter("waypointId", waypointId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
