package co.nvqa.common.core.model;

import java.util.List;
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
public class SmsNotificationsSettings {

  private Boolean allowCmi;
  private Boolean allowReturnPickupSms;
  private Boolean allowVanInbound;
  private List<Long> pickupShipperIds;
  private List<Long> shipperIds;

}
