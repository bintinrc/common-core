package co.nvqa.common.core.model.batch_update_pods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class JobUpdate {

  private Boolean toUpdateJob;
  private Long commitDate;
  private Job job;
  private Parcel parcel;
  private ProofDetails proofDetails;
  private ProofDetails proofWebhookDetails;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Parcel {

    private Long id;
    private String trackingId;
    private String parcelSize;
    private Double parcelWeight;
    private String failureReason;
    private Integer failureReasonId;
    private String action;
    private Boolean setAllowReschedule;

  }

}
