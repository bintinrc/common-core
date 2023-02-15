package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sergey Mishanin
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "shipper_pickups_search")
public class ShipperPickupSearch extends DataEntity<ShipperPickupSearch> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "address1")
  private String address1;
  @Column(name = "address2")
  private String address2;
  @Column(name = "address_id")
  private Long addressId;
  @Column(name = "approx_vol")
  private String approxVol;
  @Column(name = "city")
  private String city;
  @Column(name = "comments")
  private String comments;
  @Column(name = "contact")
  private String contact;
  @Column(name = "country")
  private String country;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "distribution_point_id")
  private Long distributionPointId;
  @Column(name = "driver_id")
  private Long driverId;
  @Column(name = "email")
  private String email;
  @Column(name = "failure_reason_id")
  private Integer failureReasonId;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "latest_datetime")
  private String latestDatetime;
  @Column(name = "legacy_shipper_id")
  private Long legacyShipperId;
  @Column(name = "marketplace_shipper_id")
  private Long marketplaceShipperId;
  @Column(name = "name")
  private String name;
  @Column(name = "postcode")
  private String postcode;
  @Column(name = "priority_level")
  private Integer priorityLevel;
  @Column(name = "ready_datetime")
  private String readyDatetime;
  @Column(name = "reservation_id")
  private Long reservationId;
  @Column(name = "reservation_type")
  private Integer reservationType;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "service_end_time")
  private String serviceEndTime;
  @Column(name = "status")
  private String status;
  @Column(name = "timewindow_id")
  private Long timewindowId;
  @Column(name = "type")
  private String type;
  @Column(name = "updated_at")
  private String updatedAt;
  @Column(name = "version")
  private Long version;
  @Column(name = "waypoint_id")
  private Long waypointId;
  @Column(name = "waypoint_status")
  private String waypointStatus;
  @Column(name = "zone_id")
  private Long zoneId;
  @Column(name = "zone_type")
  private String zoneType;

  public ShipperPickupSearch(Map<String, ?> data) {
    super(data);
  }

}