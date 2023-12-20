package co.nvqa.common.core.model.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ReservationDetailRequest {
    private Long legacyShipperId;
    private Long globalShipperId;
    private Boolean disableCutoffValidation;
    private String pickupStartTime;
    private String pickupEndTime;
    private String pickupInstruction;
    private Integer reservationTypeValue;
    private Integer priorityLevel;
}
