package co.nvqa.common.core.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

  public static final String ROUTE_TRANSFER_SCAN_EVENT = "ROUTE_TRANSFER_SCAN";
  public static final String DRIVER_INBOUND_SCAN_EVENT = "DRIVER_INBOUND_SCAN";
  public static final String ADD_TO_ROUTE_EVENT = "ADD_TO_ROUTE";
  public static final String PULL_OUT_OF_ROUTE_EVENT = "PULL_OUT_OF_ROUTE";
  public static final String ADDED_TO_RESERVATION = "ADDED_TO_RESERVATION";
  public static final String CANCEL = "CANCEL";
  public static final String UPDATE_STATUS = "UPDATE_STATUS";

  private Long orderId;
  private String type;
  private String time;
  private String userEmail;
  private EventDetail data;
  private Long userId;
  private String userName;
  private String userGrantType;
}
