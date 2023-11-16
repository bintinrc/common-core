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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "transaction_failure_reason")
public class TransactionFailureReasons extends DataEntity<TransactionFailureReasons> {

  @Id
  private Long id;
  @Column(name = "transaction_id")
  private Long transactionId;
  @Column(name = "failure_reason_code_id")
  private Short failureReasonCodeId;
  @Column(name = "failure_reason_id")
  private Long failureReasonId;

  public TransactionFailureReasons(Map<String, ?> data) {
    super(data);
  }

}
