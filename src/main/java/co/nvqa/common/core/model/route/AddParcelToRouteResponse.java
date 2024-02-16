package co.nvqa.common.core.model.route;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddParcelToRouteResponse {

  private long trackingId;
  private String status;
  private String message;

}
