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
@Table(name = "pickup_appointment_jobs_orders")
public class PickupAppointmentJobsOrders {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "pickup_appointment_job_id")
  private Long pickupAppointmentJobId;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Column(name = "deleted_at")
  private Timestamp deletedAt;
}
