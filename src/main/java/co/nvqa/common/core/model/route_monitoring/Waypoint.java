package co.nvqa.common.core.model.route_monitoring;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Waypoint {

  private String type;
  private String trackingId;
  private String timeWindow;
  private String status;
  private String serviceEndTime;
  private String name;
  private String contact;
  private String email;
  private String address;
  private String waypointStatus;
  private String timeStatus;
  private String pickupStatus;
  private Long driverLastSeen;
  private Long orderId;
  private List<String> tags;
  private Long id;
}
