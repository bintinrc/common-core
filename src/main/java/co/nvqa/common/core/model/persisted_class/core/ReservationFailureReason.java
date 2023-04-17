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
@Table(name = "reservation_failure_reason")
public class ReservationFailureReason {

  @Id
  private Long id;
  @Column(name = "reservation_id")
  private Long reservationId;
  @Column(name = "failure_reason_code_id")
  private Short failureReasonCodeId;
  @Column(name = "failure_reason_id")
  private Long failureReasonId;

}
