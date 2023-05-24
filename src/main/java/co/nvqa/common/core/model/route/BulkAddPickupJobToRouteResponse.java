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

  private List<ResponseStatusJobs> successfulPaJobs;
  private List<ResponseStatusJobs> failedPaJobs;

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ResponseStatusJobs {
    private long id;
    private String status;
    private String message;
  }
}
