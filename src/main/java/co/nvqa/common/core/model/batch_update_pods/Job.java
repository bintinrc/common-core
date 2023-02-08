package co.nvqa.common.core.model.batch_update_pods;

import co.nvqa.common.core.model.order.Order;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Job {
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
