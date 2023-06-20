package co.nvqa.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VanInboundRequest {

  private String trackingId;
  private Long waypointId;
  private String type;

}