package co.nvqa.common.core.model.pickup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pickup {

  private String address1;
  private String address2;
  private long addressId;
  private String approxVol;
  private String city;
  private String comments;
  private String contact;
  private String country;
  private long createdAt;
  private Long distributionPointId;
  private Long driverId;
  private String email;
  private Long failureReasonId;
  private long id;
  private long latestDatetime;
  private String name;
  private String postcode;
  private Integer priorityLevel;
  private long readyDatetime;
  private long reservationId;
  private Long routeId;
  private long shipperId;
  private String state;
  private String status;
  private int timewindowId;
  private String type;
  private long waypointId;
  private String waypointStatus;
}
