package co.nvqa.common.core.model.route;

import com.ctc.wstx.shaded.msv_core.util.LightStack;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParcelRouteTransferRequest {

  private Long routeId;
  private String routeDate;
  private Long fromDriverId;
  private Long toDriverId;
  private Long toDriverHubId;
  private List<Parcel> orders;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Parcel {

    private String inboundType;
    private String trackingId;
    private Long hubId;
  }
}
