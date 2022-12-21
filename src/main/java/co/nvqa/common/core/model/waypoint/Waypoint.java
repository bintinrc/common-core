package co.nvqa.common.core.model.waypoint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Waypoint {

  private Long id;
  private String status;
  private Boolean rawAddressFlag;
  private Long routeId;
  private Long seqNo;
}
