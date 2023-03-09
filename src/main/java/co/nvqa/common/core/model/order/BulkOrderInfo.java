package co.nvqa.common.core.model.order;

import co.nvqa.common.model.DataEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkOrderInfo extends DataEntity<BulkOrderInfo> {

  private Long id;
  private Long shipperId;
  private Integer qty;
  private Integer autoRescheduleDays;
  private String pickupDate;
  private Integer pickupTimewindowId;
  private Integer deliveryTimewindowId;
  private Integer hasDaynight;
  private Integer processStatus;
  private String rollbackTime;
  private String createdAt;
  private List<Order> orders;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setId(String id) {
    setId(NumberUtils.createLong(id));
  }

  public Long getShipperId() {
    return shipperId;
  }

  public void setShipperId(Long shipperId) {
    this.shipperId = shipperId;
  }

  public void setShipperId(String shipperId) {
    setShipperId(NumberUtils.createLong(shipperId));
  }

  public Integer getDeliveryTimewindowId() {
    return deliveryTimewindowId;
  }

  public void setDeliveryTimewindowId(Integer deliveryTimewindowId) {
    this.deliveryTimewindowId = deliveryTimewindowId;
  }

  public void setDeliveryTimewindowId(String deliveryTimewindowId) {
    setDeliveryTimewindowId(NumberUtils.createInteger(deliveryTimewindowId));
  }

  public Integer getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(Integer processStatus) {
    this.processStatus = processStatus;
  }

  public void setProcessStatus(String processStatus) {
    setProcessStatus(NumberUtils.createInteger(processStatus));
  }

  public Integer getHasDaynight() {
    return hasDaynight;
  }

  public void setHasDaynight(Integer hasDaynight) {
    this.hasDaynight = hasDaynight;
  }

  public void setHasDaynight(String hasDaynight) {
    if (StringUtils.equalsIgnoreCase("Yes", hasDaynight)) {
      setHasDaynight(1);
    } else if (StringUtils.equalsIgnoreCase("No", hasDaynight)) {
      setHasDaynight(0);
    } else {
      setHasDaynight(NumberUtils.createInteger(hasDaynight));
    }
  }

  public Integer getQty() {
    return qty;
  }

  public void setQty(Integer qty) {
    this.qty = qty;
  }

  public void setQty(String qty) {
    setQty(NumberUtils.createInteger(qty));
  }

  public Integer getPickupTimewindowId() {
    return pickupTimewindowId;
  }

  public void setPickupTimewindowId(Integer pickupTimewindowId) {
    this.pickupTimewindowId = pickupTimewindowId;
  }

  public void setPickupTimewindowId(String pickupTimewindowId) {
    setPickupTimewindowId(NumberUtils.createInteger(pickupTimewindowId));
  }

  public Integer getAutoRescheduleDays() {
    return autoRescheduleDays;
  }

  public void setAutoRescheduleDays(Integer autoRescheduleDays) {
    this.autoRescheduleDays = autoRescheduleDays;
  }

  public void setAutoRescheduleDays(String autoRescheduleDays) {
    setAutoRescheduleDays(NumberUtils.createInteger(autoRescheduleDays));
  }

}
