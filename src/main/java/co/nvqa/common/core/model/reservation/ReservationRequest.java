package co.nvqa.common.core.model.reservation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationRequest {
  private Long legacyShipperId;
  private String pickupStartTime;
  private String pickupEndTime;
  private String pickupApproxVolume;
  private String pickupInstruction;
  private Long pickupAddressId;
  private String pickupServiceType;
  private String pickupServiceLevel;
  private Boolean isOnDemand;
  private Integer priorityLevel;
  private Long timewindowId;
  private Boolean disableCutoffValidation;

}
