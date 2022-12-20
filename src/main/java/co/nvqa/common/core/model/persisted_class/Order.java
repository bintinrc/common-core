package co.nvqa.common.core.model.persisted_class;

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
@Table(name = "orders")
public class Order {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "tracking_id")
  private String trackingId;

  @Column(name = "status")
  private String status;

  @Column(name = "granular_status")
  private String granularStatus;

  @Column(name = "weight")
  private Double weight;

  @Column(name = "deleted_at")
  private Double deletedAt;
}
