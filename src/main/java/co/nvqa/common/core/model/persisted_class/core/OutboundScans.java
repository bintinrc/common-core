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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outbound_scans")
public class OutboundScans extends DataEntity<OutboundScans> {

  @Id
  private Long id;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "order_id")
  private Long orderId;

  public OutboundScans(Map<String, ?> data) {
    fromMap(data);
  }

}
