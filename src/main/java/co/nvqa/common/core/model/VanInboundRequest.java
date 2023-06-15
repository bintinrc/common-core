package co.nvqa.common.core.model;

public class VanInboundRequest {

  private String trackingId;
  private Long waypointId;
  private String type;

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  public Long getWaypointId() {
    return waypointId;
  }

  public void setWaypointId(Long waypointId) {
    this.waypointId = waypointId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}