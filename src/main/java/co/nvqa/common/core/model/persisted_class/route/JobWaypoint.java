package co.nvqa.common.core.model.persisted_class.route;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "job_waypoints")
public class JobWaypoint {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "system_id")
  private String systemId;

  @Column(name = "job_id")
  private Long jobId;

  @Column(name = "job_type")
  private String jobType;

  @Column(name = "waypoint_id")
  private Long waypointId;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "deleted_at")
  private Timestamp deletedAt;

}
