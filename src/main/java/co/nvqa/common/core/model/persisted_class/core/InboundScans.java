package co.nvqa.common.core.model.persisted_class.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inbound_scans")
public class InboundScans {

  @Id
  private Long id;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "order_id")
  private Long orderId;
  private String location;
  private String scan;
  private Short type;
  private String result;
  @Column(name = "scanned_by")
  private Long scannedBy;

}
