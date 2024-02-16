package co.nvqa.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RejectParcelListRequest {

  private String trackingId;
  private String comment;
  private String reason;

  public RejectParcelListRequest(String trackingId) {
    this.trackingId = trackingId;
    this.reason = "Invalid Tracking ID - does not exist in system";
    this.comment = "webhook test auto comment";
  }
}
