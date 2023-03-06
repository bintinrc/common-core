package co.nvqa.common.core.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RejectParcelListRequest {

  private String trackingId;
  private String comment;
  private String reason;

  public RejectParcelListRequest(String trackingId){
    this.trackingId = trackingId;
    this.reason = "Invalid Tracking ID - does not exist in system";
    this.comment = "webhook test auto comment";
  }
}
