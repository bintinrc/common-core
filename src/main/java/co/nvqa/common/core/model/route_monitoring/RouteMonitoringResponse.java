package co.nvqa.common.core.model.route_monitoring;

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
public class RouteMonitoringResponse {

  private List<Waypoint> waypoints;
  private List<Waypoint> parcels;
  private List<Waypoint> pickupAppointments;
  private Long routeId;
  private String driverName;
  private String zoneName;
  private String hubName;
  private int totalWaypoints;
  private Double completionPercentage;
  private int numPending;
  private int numSuccess;
  private int numValidFailed;
  private int numInvalidFailed;
  private int numEarlyWp;
  private int numLateWp;
  private int numImpending;
  private int numLateAndPending;
  private String lastSeen;
  private int totalParcels;
  private int pendingPriorityParcels;

}
