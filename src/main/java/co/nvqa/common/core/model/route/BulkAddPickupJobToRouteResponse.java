package co.nvqa.common.core.model.route;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkAddPickupJobToRouteResponse {

  private List<SuccessfulStatusJobs> successfulPaJobs;
  private List<FailedStatusJobs> failedPaJobs;

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SuccessfulStatusJobs {
    private long id;
    private String status;
  }

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FailedStatusJobs {

    private long id;
    private String message;
  }
}
