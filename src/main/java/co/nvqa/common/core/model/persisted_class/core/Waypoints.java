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

/**
 * @author Sergey Mishanin
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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

  public Waypoints(Map<String, ?> data) {
    super(data);
  }

}