package co.nvqa.common.core.model.batch_update_pods;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProofDetails {

  private String signatureImageUrl;
  private String name;
  private String contact;
  private Double latitude;
  private Double longitude;
  private List<String> trackingIds;
  private List<String> cancelledTrackingIds;
  private List<FailedParcels> failedParcels;
  private String imei;
  private int pickupQuantity;
  private String failureReason;
  private Integer failureReasonId;
  private String comments;

}
