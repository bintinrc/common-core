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

  public RouteMonitoringData(Map<String, ?> data) {
    super(data);
  }

}