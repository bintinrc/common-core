package co.nvqa.common.core.model.route;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoutesAndWaypointsRequest {

  private Long routeId;
  private Long driverId;
  private List<Long> waypointIds;
}