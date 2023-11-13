package co.nvqa.common.core.model.batch_update_pods;

import co.nvqa.common.core.model.order.Order;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Job {

  public static final String TYPE_DELIVERY = "DELIVERY";
  public static final String TYPE_TRANSACTION = "TRANSACTION";
  public static final String TYPE_RESERVATION = "RESERVATION";

  public static final String STATUS_SUCCESS = "SUCCESS";
  public static final String STATUS_FAIL = "FAIL";
  public static final String STATUS_PENDING = "PENDING";

  public static final String MODE_DELIVERY = "DELIVERY";
  public static final String MODE_PICKUP = "PICK_UP";

  private String action;
  private String confirmationCode;
  private String failureReason;
  private Integer failureReasonId;
  private Integer failureReasonCodeId;
  private Long id;
  private String mode;
  private List<Order> physicalItems;
  private String status;
  private String type;
  private Reschedule reschedule;
  private String comments;
  private Double cod;
  private Boolean allowReschedule;

  public Job() {
  }

  public Job(String action, Long id, String mode, List<Order> physicalItems, String status,
      String type) {
    this.action = action;
    this.id = id;
    this.mode = mode;
    this.physicalItems = physicalItems;
    this.status = status;
    this.type = type;
  }

}
