package co.nvqa.common.core.model.core;

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
@Table(name = "route_monitoring_data")
public class RouteMonitoringData extends DataEntity<RouteMonitoringData> {

  @Id
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "waypoint_id")
  private Long waypointId;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "data")
  private String data;
  @Column(name = "driver_name")
  private String driverName;
  @Column(name = "hub_name")
  private String hubName;
  @Column(name = "type")
  private String type;
  @Column(name = "updated_at")
  private String updatedAt;
  @Column(name = "version")
  private Integer version;
  @Column(name = "zone_name")
  private String zoneName;

  public RouteMonitoringData() {
  }

  public RouteMonitoringData(Map<String, ?> data) {
    super(data);
  }

  public Long getRouteId() {
    return routeId;
  }

  public void setRouteId(Long routeId) {
    this.routeId = routeId;
  }

  public Long getWaypointId() {
    return waypointId;
  }

  public void setWaypointId(Long waypointId) {
    this.waypointId = waypointId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  public String getHubName() {
    return hubName;
  }

  public void setHubName(String hubName) {
    this.hubName = hubName;
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

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }
}
