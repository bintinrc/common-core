package co.nvqa.common.core.model.event;

import co.nvqa.common.core.utils.EventDetailDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize(using = EventDetailDeserializer.class)
@Getter
@Setter
public class EventDetail {

  private Long shipmentId;
  private Long currHubId;
  private String currHubCountry;
  private Long destHubId;
  private Long destinationHubId; // from AV events
  private String destHubCountry;
  private Long origHubId;
  private String OrigHubCountry;
  private Long driverId;
  //get old_value & new_value of driver_id
  private EventValue driverIdValue;
  private String latitude;
  private String longitude;
  private Long routeId;
  //get old_value & new_value of route_id
  private EventValue routeIdValue;

  private Long transactionId;
  private Long waypointId;
  //get old_value & new_value of waypoint_id
  private EventValue waypointIdValue;

  private Long hubId;
  private String scanValue;
  private String[] hubLocationTypes;
  private String source;
  private Long reservationId;

  //from update status event
  private String reason;

  public static class EventValue {

    private Long oldValue;
    private Long newValue;

    public Long getOldValue() {
      return oldValue;
    }

    public void setOldValue(Long oldValue) {
      this.oldValue = oldValue;
    }

    public Long getNewValue() {
      return newValue;
    }

    public void setNewValue(Long newValue) {
      this.newValue = newValue;
    }

  }

}
