package co.nvqa.common.core.model.persisted_class;

import java.sql.Timestamp;
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
@Table(name = "order_pickups")
public class OrderPickup {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "reservation_id")
  private Long reservationId;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "deleted_at")
  private Timestamp deletedAt;

}
