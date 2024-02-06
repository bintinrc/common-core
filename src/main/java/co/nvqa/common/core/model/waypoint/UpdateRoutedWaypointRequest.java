package co.nvqa.common.core.model.waypoint;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoutedWaypointRequest {

  private List<Waypoint> waypoints;
}
