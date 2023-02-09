package co.nvqa.common.core.model.core;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sergey Mishanin
 */
@Entity
@Table(name = "waypoints")
public class Waypoints extends DataEntity<Waypoints> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "address1")
  private String address1;
  @Column(name = "address2")
  private String address2;
  @Column(name = "city")
  private String city;
  @Column(name = "cluster_id")
  private Integer clusterId;
  @Column(name = "clusterable")
  private Integer clusterable;
  @Column(name = "country")
  private String country;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "date")
  private Date date;
  @Column(name = "estimated_time_needed")
  private Long estimatedTimeNeeded;
  @Column(name = "eta_sent")
  private Integer etaSent;
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;
  @Column(name = "pickup_quantity")
  private Long pickupQuantity;
  @Column(name = "postcode")
  private String postcode;
  @Column(name = "recommended_cluster_id")
  private Integer recommendedClusterId;
  @Column(name = "recommended_timeslot_id")
  private Integer recommendedTimeslotId;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "routing_zone_id")
  private Long routingZoneId;
  @Column(name = "seq_no")
  private Long seqNo;
  @Column(name = "status")
  private String status;
  @Column(name = "timeslot_id")
  private Long timeslotId;
  @Column(name = "timewindow_id")
  private Long timewindowId;
  @Column(name = "updated_at")
  private String updatedAt;
  @Column(name = "waypoint_type")
  private Integer waypointType;
  @Column(name = "zone_type")
  private String zoneType;


  public Waypoints() {
  }

  public Waypoints(Map<String, ?> data) {
    super(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(String deletedAt) {
    this.deletedAt = deletedAt;
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

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Integer getClusterId() {
    return clusterId;
  }

  public void setClusterId(Integer clusterId) {
    this.clusterId = clusterId;
  }

  public Integer getClusterable() {
    return clusterable;
  }

  public void setClusterable(Integer clusterable) {
    this.clusterable = clusterable;
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Long getEstimatedTimeNeeded() {
    return estimatedTimeNeeded;
  }

  public void setEstimatedTimeNeeded(Long estimatedTimeNeeded) {
    this.estimatedTimeNeeded = estimatedTimeNeeded;
  }

  public Integer getEtaSent() {
    return etaSent;
  }

  public void setEtaSent(Integer etaSent) {
    this.etaSent = etaSent;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Long getPickupQuantity() {
    return pickupQuantity;
  }

  public void setPickupQuantity(Long pickupQuantity) {
    this.pickupQuantity = pickupQuantity;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public Integer getRecommendedClusterId() {
    return recommendedClusterId;
  }

  public void setRecommendedClusterId(Integer recommendedClusterId) {
    this.recommendedClusterId = recommendedClusterId;
  }

  public Integer getRecommendedTimeslotId() {
    return recommendedTimeslotId;
  }

  public void setRecommendedTimeslotId(Integer recommendedTimeslotId) {
    this.recommendedTimeslotId = recommendedTimeslotId;
  }

  public Long getRouteId() {
    return routeId;
  }

  public void setRouteId(Long routeId) {
    this.routeId = routeId;
  }

  public Long getRoutingZoneId() {
    return routingZoneId;
  }

  public void setRoutingZoneId(Long routingZoneId) {
    this.routingZoneId = routingZoneId;
  }

  public Long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Long seqNo) {
    this.seqNo = seqNo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getTimeslotId() {
    return timeslotId;
  }

  public void setTimeslotId(Long timeslotId) {
    this.timeslotId = timeslotId;
  }

  public Long getTimewindowId() {
    return timewindowId;
  }

  public void setTimewindowId(Long timewindowId) {
    this.timewindowId = timewindowId;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Integer getWaypointType() {
    return waypointType;
  }

  public void setWaypointType(Integer waypointType) {
    this.waypointType = waypointType;
  }

  public String getZoneType() {
    return zoneType;
  }

  public void setZoneType(String zoneType) {
    this.zoneType = zoneType;
  }
}
