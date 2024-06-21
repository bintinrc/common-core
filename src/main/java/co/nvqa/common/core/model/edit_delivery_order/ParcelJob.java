package co.nvqa.common.core.model.edit_delivery_order;

import co.nvqa.common.core.model.order.Stamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelJob {

  private String deliveryDate;
  private int deliveryTimewindow;
  private Stamp stamp;
  private double insurance;
  private String deliveryInstruction;
  private String orderInstruction;
  private String pickupInstruction;
}
