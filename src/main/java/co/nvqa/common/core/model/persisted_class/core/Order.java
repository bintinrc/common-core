package co.nvqa.common.core.model.persisted_class.core;

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
  private Long id;

  @Column(name = "tracking_id")
  private String trackingId;

  private String status;

  @Column(name = "granular_status")
  private String granularStatus;

  private Double weight;

  @Column(name = "deleted_at")
  private Double deletedAt;
}
