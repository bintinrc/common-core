package co.nvqa.common.core.model.reservation;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 5/18/17.
 *
 * @author felixsoewito
 * <p>
 * JSON format: camel case // v2: snake case
 */
@Setter
@Getter
public class ReservationResponse {

  private Integer statusValue;
  private Integer reservationTypeValue;
  private Long legacyShipperId;
  private String name;
  private String email;
  private String contact;
  private Boolean onDemand;
  private Long distributionPointId;
  private Long lodgeInDistributionPointId;
  private Long addressId;
  private String readyDatetime;
  private String latestDatetime;
  private Long waypointId;
  private String approxVolume;
  private String comments;
  private Integer priorityLevel;
  private String readyTime;
  private String latestTime;
  private String confirmCode;
  private Long id;
  private Map<String, Object> metadata;
  private String merchantBookingRef;
  private Waypoint activeWaypoint;

  @Setter
  @Getter
  public static class Waypoint {

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ROUTED = "routed";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";

    public Long id;
    public String address1;
    public String address2;
    public String city;
    public String country;
    public String postcode;
    public Object activeRouteId;
  }
}
