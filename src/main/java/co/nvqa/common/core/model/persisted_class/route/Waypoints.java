package co.nvqa.common.core.model.persisted_class.route;

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
  @Column(name = "address1")
  private String address1;
  @Column(name = "address2")
  private String address2;
  @Column(name = "city")
  private String city;
  @Column(name = "country")
  private String country;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "date")
  private Date date;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "legacy_id")
  private Long legacyId;
  @Column(name = "longitude")
  private Double longitude;
  @Column(name = "postcode")
  private String postcode;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "routing_zone_id")
  private Long routingZoneId;
  @Column(name = "seq_no")
  private Long seqNo;
  @Column(name = "status")
  private String status;
  @Column(name = "system_id")
  private String systemId;
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