package co.nvqa.common.core.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetails {

  private ParcelJob parcelJob;
  private UserDetails to;
  private UserDetails from;

  public DeliveryDetails(ParcelJob parcelJob, UserDetails to) {
    this.parcelJob = parcelJob;
    this.to = to;
  }

}
