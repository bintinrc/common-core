package co.nvqa.common.core.model.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 12/28/16.
 *
 * @author felixsoewito
 * <p>
 * JSON format: camel case, but snake case in Order V2
 */
@Setter
@Getter
public class Transaction implements Serializable {

  public static final String TYPE_PICKUP = "PICKUP";
  public static final String TYPE_DELIVERY = "DELIVERY";
  public static final String TYPE_PP = "PP";
  public static final String TYPE_DD = "DD";

  public static final String STATUS_PENDING = "pending";
  public static final String STATUS_SUCCESS = "success";
  public static final String STATUS_FAIL = "fail";

  private Long distributionPointId;
  private Long dnrId;
  private String driverName;
  private String email;
  private String endTime;
  private Long id;
  private String instruction;
  private String name;
  private Long orderId;
  private String address1;
  private String address2;
  private String postcode;
  private Long routeId;
  private String startTime;
  private String status;
  private Boolean transit;
  private String type;
  private Long waypointId;
  private String dnr;
  private String serviceEndTime;
  private Long priorityLevel;
  private Long routingZoneId;
  private String comments;
  private Order order;
  private Integer seqNo;

  @JsonAlias({"failure_reason_code", "failureReasonCode"})
  private String failureReasonCode;
}
