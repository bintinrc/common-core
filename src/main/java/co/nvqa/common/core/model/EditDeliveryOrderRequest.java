package co.nvqa.common.core.model;

import co.nvqa.common.core.model.edit_delivery_order.ParcelJob;
import co.nvqa.common.core.model.edit_delivery_order.RecipientDetail;
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
public class EditDeliveryOrderRequest {

  private ParcelJob parcelJob;
  private RecipientDetail to;
}
