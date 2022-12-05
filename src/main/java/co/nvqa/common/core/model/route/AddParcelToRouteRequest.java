package co.nvqa.common.core.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddParcelToRouteRequest {

  private String type; // DELIVERY / PICKUP
  private Long routeId;
}
