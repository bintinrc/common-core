package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.JobWaypointDao;
import co.nvqa.common.core.model.persisted_class.JobWaypoint;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.java.en.When;

import javax.inject.Inject;

public class DbJobWaypointsSteps extends CoreStandardSteps {

  @Inject
  private JobWaypointDao jobWaypointDao;

  @Override
  public void init() {
  }

  /**
   * This method gets the waypoint_id if by job_id
   *
   * @param StringJobId KEY contains order id
   */
  @When("DB Core - get waypoint id for job id {string}")
  public void getWaypointIdByJobId(String StringJobId) {
    final long jobId = Long.parseLong(resolveValue(StringJobId));
    final JobWaypoint jobWaypoint = retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      final JobWaypoint result = jobWaypointDao.getWaypointIdByJobId(jobId);
      if (result == null) {
        throw new NvTestRuntimeException("waypoint is not found for job id " + jobId);
      }
      return result;
    }, "reading job waypoint from job id: " + jobId);
    put(KEY_WAYPOINT_ID, jobWaypoint.getWaypointId());
  }
}
