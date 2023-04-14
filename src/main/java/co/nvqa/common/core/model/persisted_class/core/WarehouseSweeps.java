package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
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
@Table(name = "warehouse_sweeps")
public class WarehouseSweeps extends DataEntity<WarehouseSweeps> {

  @Id
  private Long id;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "order_id")
  private Long orderId;
  private String scan;
  @Column(name = "scanned_by")
  private Long scannedBy;

}
