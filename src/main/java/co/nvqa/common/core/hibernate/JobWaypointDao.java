package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.JobWaypoint;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;

import java.util.List;
import javax.inject.Singleton;

@Singleton
public class JobWaypointDao extends DbBase {

  public JobWaypointDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public JobWaypoint getWaypointIdByJobId(Long jobId) {
    List<JobWaypoint> results;
    String query = "FROM JobWaypoint "
        + "WHERE jobId = :jobId";
    results = findAll(session ->
        session.createQuery(query, JobWaypoint.class)
            .setParameter("jobId", jobId));
    return results.get(0);
  }
}
