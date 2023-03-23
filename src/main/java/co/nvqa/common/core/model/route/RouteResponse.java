package co.nvqa.common.core.model.route;

import co.nvqa.common.model.DataEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created on 5/18/17.
 *
 * @author felixsoewito
 * <p>
 * JSON format: camel case
 */
@Setter
@Getter
public class RouteResponse {

  private Long id;
  private String createdAt;
  private Driver driver;
  private List<Integer> tags = new ArrayList<>();

  private Boolean archived;
  private String comments;
  @JsonIgnore
  private Date createdAtAsDate;
  private String deletedAt;
  private String date;
  private String dateTime;
  private Long driverId;
  private String driverName;
  private Integer flags;
  private Long hubId;
  private String largestParcelSize;
  private String routePassword;
  private Integer type;
  private Vehicle vehicle;
  private Long vehicleId;
  private List<Waypoint> waypoints = new ArrayList<>();
  private Long zoneId;

  @Setter
  @Getter
  public static class Driver {

    private String uuid;
    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String driverType;
    private Integer driverTypeId;
    private Boolean availability;
    private Integer codLimit;
    private Integer maxOnDemandJobs;
    private String username;
    private String password;
    private String comments;
    private String hub;
    private Integer maxBiddableWaypoints;
    private Long hubId;
    private List<Vehicle> vehicles;
    private List<Contact> contacts;
    private List<ZonePreferences> zonePreferences;
    private Map<String, Object> tags;
    private String employmentStartDate;
  }

  @Setter
  @Getter
  public static class Vehicle {

    private String createdAt;
    private String deletedAt;
    private Long id;
    private String updatedAt;
    private String vehicleNo;
    private Integer capacity;
    private Boolean active;
    private String vehicleType;
    private Boolean ownVehicle;
  }

  @Setter
  @Getter
  public static class Contact {

    private Boolean active;
    private String type;
    private String details;
  }

  @Setter
  @Getter
  public static class ZonePreferences {

    private Double latitude;
    private Double longitude;
    private Integer rank;
    private Long zoneId;
    private Integer minWaypoints;
    private Integer maxWaypoints;
    private Integer cost;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Waypoint extends DataEntity<Waypoint>{
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ROUTED = "routed";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";

    private Long id;
    private String city;
    private String address1;
    private String address2;
    private String contact;
    private String country;
    private String postcode;
    private Double latitude;
    private Double longitude;
    private String status;
    private String timeWindowId;
    private String date;
    private Long routeId;
    private Long seqNo;
    private Long waypointType;
    private Long routingZoneId;

    public Waypoint(Map<String, ?> data) {
      super(data);
    }

    public Waypoint(Long id) {
      this.id = id;
    }
  }
}
