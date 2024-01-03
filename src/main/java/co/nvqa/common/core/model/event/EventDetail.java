package co.nvqa.common.core.model.event;

import co.nvqa.common.core.utils.EventDetailDeserializer;
import co.nvqa.common.model.DataEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonDeserialize(using = EventDetailDeserializer.class)
@Getter
@Setter
public class EventDetail extends DataEntity<EventDetail> {

  private Long shipmentId;
  private Long currHubId;
  private String currHubCountry;
  private Long destHubId;
  private Long destinationHubId; // from AV events
  private String destHubCountry;
  private Long origHubId;
  private String origHubCountry;
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
  private EventValueString granularStatus;
  private EventValueString orderStatus;

  //get auto AV event
  private String status;
  private String mode;

  //order Dimensions
  private EventValue weight;
  private EventValue length;
  private EventValue height;
  private EventValue width;

  //ticket resolution
  private Long ticketId;
  private String orderOutcome;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EventValue {

    private Long oldValue;
    private Long newValue;

  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EventValueString {

    private String oldValue;
    private String newValue;

  }

}
