package co.nvqa.common.core.model.persisted_class.routing_search;

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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "transactions")
public class Transactions extends DataEntity<Transactions> {

  @Id
  private Long id;
  @Column(name = "txn_id")
  private Long txnId;
  @Column(name = "system_id")
  private String systemId;
  @Column(name = "txn_type")
  private String txnType;
  @Column(name = "txn_status")
  private String txnStatus;
  @Column(name = "dnr_id")
  private Integer dnrId;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "tracking_id")
  private String trackingId;
  @Column(name = "order_type")
  private String orderType;
  @Column(name = "granular_status")
  private String granularStatus;
  private Integer rts;
  @Column(name = "waypoint_id")
  private Long waypointId;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "routing_zone_id")
  private Long routingZoneId;
  @Column(name = "zone_type")
  private String zoneType;
  @Column(name = "deleted_at")
  private String deletedAt;

  public Transactions(Map<String, ?> data) {
    super(data);
  }
}


