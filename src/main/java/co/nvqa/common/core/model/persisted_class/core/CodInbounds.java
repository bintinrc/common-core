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
@Table(name = "cod_inbounds")
public class CodInbounds extends DataEntity<CodInbounds> {

  @Id
  private Long id;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "amt_collected")
  private Double amountCollected;
  @Column(name = "receipt_no")
  private String receiptNo;
  private String type;
  @Column(name = "deleted_at")
  private String deletedAt;

  public CodInbounds(Map<String, ?> data) {
    fromMap(data);
  }
}
