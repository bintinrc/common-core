package co.nvqa.common.core.model.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
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
}
