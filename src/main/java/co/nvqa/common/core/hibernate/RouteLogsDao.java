package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.RouteLogs;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class RouteLogsDao extends DbBase {

  public RouteLogsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public RouteLogs getRouteLogs(Long id) {
    String query = "FROM RouteLogs WHERE id = :id";
    List<RouteLogs> result = findAll(session ->
        session.createQuery(query, RouteLogs.class)
            .setParameter("id", id));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public List<RouteLogs> getMultipleRouteLogsbyDriverId(Long driverId) {
    String query = "FROM RouteLogs WHERE driverId = :driverId AND archived = 0 AND deletedAt IS NULL";
    return findAll(session ->
        session.createQuery(query, RouteLogs.class)
            .setParameter("driverId", driverId));
  }

}
