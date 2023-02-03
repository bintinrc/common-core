package co.nvqa.common.core.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsNotificationsSettings {

  private Boolean allowCmi;
  private Boolean allowReturnPickupSms;
  private Boolean allowVanInbound;
  private List<Long> pickupShipperIds;
  private List<Long> shipperIds;

}
