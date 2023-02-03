package co.nvqa.common.core.model.event;

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

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public EventDetail getData() {
    return data;
  }

  public void setData(EventDetail data) {
    this.data = data;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserGrantType() {
    return userGrantType;
  }

  public void setUserGrantType(String userGrantType) {
    this.userGrantType = userGrantType;
  }
}
