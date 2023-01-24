package co.nvqa.common.core.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JSON example at Jan 24th 2023:
 * success response:
 * <p>
 *   {"tracking_id":"LZWP4LD9Q0M99","status":"Success","order_id":843241457,"message":"Rescheduled LZWP4LD9Q0M99."}
 * </p>
 * fail response:
 * <p>
 *   {"tracking_id":null,"status":"Fail","order_id":843241457,"message":"exceptions.ProcessingException: Current order state is Arrived at Sorting Hub, is not in Pending Reschedule state [OrderId:843241457]"}
 * </p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleOrderResponse {

  private String message;
  private long orderId;
  private String status; // check the status to verify the success of reschedule!
  private String trackingId;
}
