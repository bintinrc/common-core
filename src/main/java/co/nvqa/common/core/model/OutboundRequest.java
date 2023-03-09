package co.nvqa.common.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutboundRequest {

  private Long hubId;
  private Long routeId;
  private List<String> successScans; // List of Tracking IDs
  private List<String> excludedScans;
  private List<String> missingScans;

  public OutboundRequest(String trackingId, Long hubId, Long routeId) {
    this.successScans = new ArrayList<>();
    successScans.add(trackingId);
    this.hubId = hubId;
    this.routeId = routeId;
    this.excludedScans = Collections.emptyList();
    this.missingScans = Collections.emptyList();
  }

  public void addSuccessScans(String trackingId) {
    if (successScans == null) {
      successScans = new ArrayList<>();
    }
    successScans.add(trackingId);
  }
}
