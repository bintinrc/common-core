package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.CoreRouteLogs;
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

  public CoreRouteLogs getRouteLogs(Long id) {
    String query = "FROM CoreRouteLogs WHERE id = :id";
    List<CoreRouteLogs> result = findAll(session ->
        session.createQuery(query, CoreRouteLogs.class)
            .setParameter("id", id));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public List<CoreRouteLogs> getMultipleRouteLogsbyDriverId(Long driverId) {
    String query = "FROM CoreRouteLogs WHERE driverId = :driverId AND archived = 0 AND deletedAt IS NULL";
    return findAll(session ->
        session.createQuery(query, CoreRouteLogs.class)
            .setParameter("driverId", driverId));
  }

}
