package co.nvqa.common.core.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddParcelToRouteRequest {

  public static final String TYPE_PICKUP = "PICKUP";
  public static final String TYPE_DELIVERY = "DELIVERY";
  public static final String PP = "PP";
  public static final String DD = "DD";

  private String type; // DELIVERY / PICKUP
  private Long routeId;
  private String trackingId;

  public AddParcelToRouteRequest(String trackingId, String type, Long routeId) {
    this.trackingId = trackingId;
    if (type.equals(PP)) {
      this.type = TYPE_PICKUP;
    } else if (type.equals(DD)) {
      this.type = TYPE_DELIVERY;
    }
    this.routeId = routeId;
  }

  public void setType(String type) {
    if (type.equals(PP)) {
      this.type = TYPE_PICKUP;
    } else if (type.equals(DD)) {
      this.type = TYPE_DELIVERY;
    } else {
      this.type = type;
    }
  }
}
