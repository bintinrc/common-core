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
@Table(name = "cods")
public class Cods extends DataEntity<Cods> {

  @Id
  private Long id;
  @Column(name = "audit_log")
  private String auditLog;
  @Column(name = "collection_at")
  private String collectionAt;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "deposit_id")
  private String depositId;
  @Column(name = "edited")
  private String edited;
  @Column(name = "goods_amount")
  private Double goodsAmount;
  @Column(name = "shipping_amount")
  private String shippingAmount;
  @Column(name = "updated_at")
  private String updatedAt;

  public Cods(Map<String, ?> data) {
    super(data);
  }

}
