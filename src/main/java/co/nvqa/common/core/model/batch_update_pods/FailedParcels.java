package co.nvqa.common.core.model.batch_update_pods;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FailedParcels {

  private Long orderId;
  private Integer failureReasonId;
  private String failureReason;

}
