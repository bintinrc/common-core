package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
import java.util.Map;
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
@Table(name = "transactions")
public class Transactions extends DataEntity<Transactions> {

  @Id
  private Long id;

  private String type;

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "waypoint_id")
  private Long waypointId;

  @Column(name = "start_time")
  private String startTime;

  @Column(name = "end_time")
  private String endTime;

  private String status;

  private String name;

  private String email;

  private String contact;

  private String address1;

  private String address2;

  private String city;

  private String country;

  private String postcode;

  @Column(name = "distribution_point_id")
  private Integer distributionPointId;

  @Column(name = "route_id")
  private Long routeId;

  @Column(name = "priority_level")
  private Integer priorityLevel;

  @Column(name = "service_end_time")
  private Date serviceEndTime;

  @Column(name = "deleted_at")
  private String deletedAt;

  @Column(name = "dnr_id")
  private Integer dnrId;

  public Transactions(Map<String, ?> data) {
    super(data);
  }
}
