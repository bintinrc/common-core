package co.nvqa.common.core.model.batch_update_pods;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailedParcels {

  private Long orderId;
  private Integer failureReasonId;
  private String failureReason;

}
