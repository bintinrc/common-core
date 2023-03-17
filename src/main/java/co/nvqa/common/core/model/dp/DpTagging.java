package co.nvqa.common.core.model.dp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DpTagging {

  private AddToRoute addToRoute;
  private DpTag dpTag;

  //  the rest of these fields are for response only
  private Long dpId;
  private String message;
  private Long orderId;
  private String status;
  private String trackingId;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AddToRoute {

    private Long routeId;
    private String type;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DpTag {

    private Long dpId;
    private String authorizedBy;
    private String collectBy;
    private String dpServiceType;
    private String dropOffOn;
    private String endDate;
    private String reason;
    private Boolean shouldReserveSlot;
    private Boolean skipATLValidation;
    private String startDate;
  }
}
