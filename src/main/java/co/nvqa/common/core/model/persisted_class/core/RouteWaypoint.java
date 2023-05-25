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
@Table(name = "route_waypoint")
public class RouteWaypoint extends DataEntity<RouteWaypoint> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "seq_no")
  private Long seqNo;
  @Column(name = "updated_at")
  private String updatedAt;
  @Column(name = "waypoint_id")
  private Long waypointId;

  public RouteWaypoint(Map<String, ?> data) {
    super(data);
  }

}