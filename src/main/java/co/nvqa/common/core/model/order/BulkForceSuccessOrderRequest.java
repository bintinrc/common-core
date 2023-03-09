package co.nvqa.common.core.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkForceSuccessOrderRequest {

  private Long orderId;
  private Boolean isCodCollected;
  private String reason;

}
