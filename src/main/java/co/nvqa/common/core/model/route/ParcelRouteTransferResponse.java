package co.nvqa.common.core.model.route;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParcelRouteTransferResponse {

  private List<FailedOrders> failedOrders;
  private List<RouteResponse> routes;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FailedOrders {

    private String reason;
    private List<String> trackingIds;
  }
}
