package co.nvqa.common.core.model.route;

import com.fasterxml.jackson.annotation.JsonProperty;
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

  private Data data;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Data {

    private List<SuccessOrder> success;
    private List<FailedOrder> failure;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SuccessOrder {

    @JsonProperty("tracking_id")
    private String trackingId;
    @JsonProperty("route_id")
    private long routeId;
    @JsonProperty("waypoint_id")
    private long waypointId;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FailedOrder {

    @JsonProperty("tracking_id")
    private String trackingId;
    private String reason;
  }
}
