package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sergey Mishanin
 */
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


  public ShipperPickupSearch() {
  }

  public ShipperPickupSearch(Map<String, ?> data) {
    super(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public Long getAddressId() {
    return addressId;
  }

  public void setAddressId(Long addressId) {
    this.addressId = addressId;
  }

  public String getApproxVol() {
    return approxVol;
  }

  public void setApproxVol(String approxVol) {
    this.approxVol = approxVol;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public Long getDistributionPointId() {
    return distributionPointId;
  }

  public void setDistributionPointId(Long distributionPointId) {
    this.distributionPointId = distributionPointId;
  }

  public Long getDriverId() {
    return driverId;
  }

  public void setDriverId(Long driverId) {
    this.driverId = driverId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getFailureReasonId() {
    return failureReasonId;
  }

  public void setFailureReasonId(Integer failureReasonId) {
    this.failureReasonId = failureReasonId;
  }

  public Long getHubId() {
    return hubId;
  }

  public void setHubId(Long hubId) {
    this.hubId = hubId;
  }

  public String getLatestDatetime() {
    return latestDatetime;
  }

  public void setLatestDatetime(String latestDatetime) {
    this.latestDatetime = latestDatetime;
  }

  public Long getLegacyShipperId() {
    return legacyShipperId;
  }

  public void setLegacyShipperId(Long legacyShipperId) {
    this.legacyShipperId = legacyShipperId;
  }

  public Long getMarketplaceShipperId() {
    return marketplaceShipperId;
  }

  public void setMarketplaceShipperId(Long marketplaceShipperId) {
    this.marketplaceShipperId = marketplaceShipperId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public Integer getPriorityLevel() {
    return priorityLevel;
  }

  public void setPriorityLevel(Integer priorityLevel) {
    this.priorityLevel = priorityLevel;
  }

  public String getReadyDatetime() {
    return readyDatetime;
  }

  public void setReadyDatetime(String readyDatetime) {
    this.readyDatetime = readyDatetime;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public void setReservationId(Long reservationId) {
    this.reservationId = reservationId;
  }

  public Integer getReservationType() {
    return reservationType;
  }

  public void setReservationType(Integer reservationType) {
    this.reservationType = reservationType;
  }

  public Long getRouteId() {
    return routeId;
  }

  public void setRouteId(Long routeId) {
    this.routeId = routeId;
  }

  public String getServiceEndTime() {
    return serviceEndTime;
  }

  public void setServiceEndTime(String serviceEndTime) {
    this.serviceEndTime = serviceEndTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getTimewindowId() {
    return timewindowId;
  }

  public void setTimewindowId(Long timewindowId) {
    this.timewindowId = timewindowId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getWaypointId() {
    return waypointId;
  }

  public void setWaypointId(Long waypointId) {
    this.waypointId = waypointId;
  }

  public String getWaypointStatus() {
    return waypointStatus;
  }

  public void setWaypointStatus(String waypointStatus) {
    this.waypointStatus = waypointStatus;
  }

  public Long getZoneId() {
    return zoneId;
  }

  public void setZoneId(Long zoneId) {
    this.zoneId = zoneId;
  }

  public String getZoneType() {
    return zoneType;
  }

  public void setZoneType(String zoneType) {
    this.zoneType = zoneType;
  }
}
