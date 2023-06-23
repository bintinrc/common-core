package co.nvqa.common.core.model.edit_delivery_order;

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
}
