package co.nvqa.common.core.model.order;

import co.nvqa.common.core.model.order.Order.Dimension;
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
public class ParcelJob {

  private String deliveryDate;
  private Integer deliveryTimewindow;
  private Boolean shipperRequested;
  private String pickupDate;
  private Integer pickupTimewindow;
  private Dimension dimensions;
  private Double insurance;

  public ParcelJob(String deliveryDate, Integer deliveryTimewindow, boolean shipperRequested) {
    this.deliveryDate = deliveryDate;
    this.deliveryTimewindow = deliveryTimewindow;
    this.shipperRequested = shipperRequested;
  }

  public boolean isShipperRequested() {
    return shipperRequested;
  }

  public void setShipperRequested(boolean shipperRequested) {
    this.shipperRequested = shipperRequested;
  }

}
