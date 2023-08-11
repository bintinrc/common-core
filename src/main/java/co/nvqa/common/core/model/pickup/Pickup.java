package co.nvqa.common.core.model.pickup;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pickup extends DataEntity<Pickup> {

  private String address1;
  private String address2;
  private Long addressId;
  private String approxVol;
  private String city;
  private String comments;
  private String contact;
  private String country;
  private Long createdAt;
  private Long distributionPointId;
  private Long driverId;
  private String email;
  private Long failureReasonId;
  private Long id;
  private Long latestDatetime;
  private Long readyDatetime;
  private String name;
  private String postcode;
  private Integer priorityLevel;
  private Long reservationId;
  private Long routeId;
  private Long shipperId;
  private String state;
  private String status;
  private Integer timewindowId;
  private String type;
  private Long waypointId;
  private String waypointStatus;
  private List<Pod> pods;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Pod {

    private Long id;
    private String name;
    private Date timestamp;
    private Integer submittedScansQuantity;
    private Integer shipperScansQuantity;
    private List<String> shipperScans;
    private Integer hubScansQuantity;
    private List<String> hubScans;
    private String signatureUrl;
  }

  public Pickup(Map<String, ?> resolvedData) {
    fromMap(resolvedData);
  }
}
