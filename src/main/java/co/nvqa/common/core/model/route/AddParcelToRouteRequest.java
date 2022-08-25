package co.nvqa.common.core.model.route;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Daniel Joi Partogi Hutapea
 * <p>
 * JSON format: snake case
 */
@Setter
@Getter
public class AddParcelToRouteRequest {

  private String trackingId;
  private String type;
  private Long routeId;

  public AddParcelToRouteRequest() {
  }

  public AddParcelToRouteRequest(String trackingId, String type, Long routeId) {
    this.trackingId = trackingId;
    this.type = type;
    this.routeId = routeId;
  }
}
