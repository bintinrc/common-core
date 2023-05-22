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

  private List<successfulPaJob> successfulPaJobs;
  private List<failedPaJob> failedPaJobs;

  public static class successfulPaJob {

    private long id;
    private String status;
  }

  public static class failedPaJob {

    private long id;
    private String status;
  }
}
