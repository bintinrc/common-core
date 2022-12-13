package co.nvqa.common.core.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddPickupJobToRouteRequest {

  private Long newRouteId;
  private Boolean overwrite;
}
