package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.RescheduleOrderRequest;
import co.nvqa.common.core.model.order.RescheduleOrderResponse;
import co.nvqa.common.core.model.order.RtsOrderRequest;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class ApiOrderSteps extends CoreStandardSteps {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderSteps.class);

  @Inject
  @Getter
  private OrderClient orderClient;

  @Override
  public void init() {

  }

  /**
   * <br/><b>Output key</b>: <ul><li>KEY_LIST_OF_CREATED_ORDERS: list of orders</li></ul>
   * <p>
   * read the order state and put it into the scenario manager in list form, it replace the previous
   * order with the same tracking id. <br/><br/><b>Note</b>: becareful that you may face unintended
   * order status due to event propagation delay to Core service
   *
   * @param tracking key that contains order's tracking id, example: KEY_LIST_OF_CREATED_TRACKING_IDS
   */
  @When("API Core - Operator get order details for tracking order {string}")
  public void apiOperatorGetOrderDetailsForTrackingOrder(String tracking) {
    final String trackingId = resolveValue(tracking);
    final Order order = retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getOrderClient().searchOrderByTrackingId(trackingId),
        "Order client search order by tracking id");

    putInList(KEY_LIST_OF_CREATED_ORDERS, order,
        (o1, o2) -> StringUtils.equalsAnyIgnoreCase(o1.getTrackingId(), o2.getTrackingId()));
  }

  @When("API Core - Operator get multiple order details for tracking ids:")
  public void apiOperatorGetMultipleOrderDetails(List<String> trackingIds) {
    trackingIds.forEach(e -> apiOperatorGetOrderDetailsForTrackingOrder(e));
  }

  /**
   * <br/><b>Output key</b>: <ul><li>KEY_LIST_OF_CREATED_ORDERS: list of orders</li></ul>
   * <p>
   * read the order state and put it into the scenario manager in list form, it wont replace the
   * previous order with the same tracking id. this is intended to check if you have done certain
   * action to same order and you need previous data prior to the action being done
   *
   * @param tracking key that contains order's tracking id, example: KEY_LIST_OF_CREATED_TRACKING_IDS
   */
  @When("API Core - Operator get order details for previous order {string}")
  public void apiOperatorGetOrderDetailsForPreviousOrder(String tracking) {
    final String trackingId = resolveValue(tracking);
    final Order order = retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getOrderClient().searchOrderByTrackingId(trackingId),
        "Order client search order by tracking id");
    
    putInList(KEY_LIST_OF_CREATED_ORDERS, order);
  }

  /**
   * <br/><b>Output key</b>: <ul><li>KEY_LIST_OF_CREATED_ORDERS: list of orders</li></ul>
   * <p>
   * similar as the other step, but this step will re attempt to read the latest parcel info until
   * it reach the expected granular status, it won't throw any exception and warn the user if the
   * expected granular status never reached
   * <p>
   * {@link ApiOrderSteps#apiOperatorGetOrderDetailsForTrackingOrder(String)}
   *
   * @param tracking               key that contains order's tracking id, example:
   *                               KEY_LIST_OF_CREATED_TRACKING_IDS
   * @param expectedGranularStatus PENDING PICKUP, ARRIVED AT SORTING HUB, ...
   */
  @When("API Core - Operator get order details for tracking order {string} with granular status {string}")
  public void apiOperatorGetOrderDetailsForTrackingOrderWithGranularStatus(String tracking,
      String expectedGranularStatus) {
    final String trackingId = resolveValue(tracking);

    try {
      final Order order = retryIfAssertionErrorOrRuntimeExceptionOccurred(
          () -> {
            final Order tempOrder = getOrderClient().searchOrderByTrackingId(trackingId);
            final String actualGranularStatus = tempOrder.getGranularStatus();
            if (!StringUtils.equalsIgnoreCase(actualGranularStatus, expectedGranularStatus)) {
              final String message = f("Order actual status is not match, expected: %s but was: %s",
                  expectedGranularStatus, actualGranularStatus);
              LOGGER.debug(message);
              throw new NvTestRuntimeException(message);
            }
            return tempOrder;
          },
          "Order client search order by tracking id");

      putInList(KEY_LIST_OF_CREATED_ORDERS, order,
          (o1, o2) -> StringUtils.equalsAnyIgnoreCase(o1.getTrackingId(), o2.getTrackingId()));
    } catch (NvTestRuntimeException ex) {
      LOGGER.warn(
          "failed to get the order with the expected granular status! cause: " + ex.getMessage());
    }
  }

  /**
   * API Core - Operator force success order "{KEY_LIST_OF_CREATED_ORDERS[1].id}"
   *
   * @param orderIdString example: {KEY_LIST_OF_CREATED_ORDERS[1].id}
   */
  @When("API Core - Operator force success order {string}")
  public void apiOperatorForceSuccessOrder(String orderIdString) {
    apiOperatorForceSuccessOrder(orderIdString, "true");
  }

  /**
   * API Core - Operator force success order "{KEY_LIST_OF_CREATED_ORDERS[1].id}" with cod collected
   * "false"
   *
   * @param orderIdString      example: {KEY_LIST_OF_CREATED_ORDERS[1].id}
   * @param codCollectedString example: false
   */
  @When("API Core - Operator force success order {string} with cod collected {string}")
  public void apiOperatorForceSuccessOrder(String orderIdString, String codCollectedString) {
    final long orderId = Long.parseLong(resolveValue(orderIdString));
    final boolean codCollected = Boolean.parseBoolean(codCollectedString);
    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getOrderClient().forceSuccess(orderId, codCollected), "Force success", 3000, 10);
  }

  /**
   * Unless reattempt is false, this method will re-attempt the action until response message is
   * success
   * <p></p>
   *
   * @param dataTableRaw <br/> <b>orderId</b>: order ID of the order/parcel<br/>
   *                     <b>rescheduleRequest</b> {"date":"{date: 0 days ago, yyyy-MM-dd}"}<br/>
   *                     <b>[OPTIONAL] reattempt</b> true (default) | false"}
   */
  @When("API Core - Operator reschedule order:")
  public void apiOperatorRescheduleOrder(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final String rescheduleRequest = dataTable.get("rescheduleRequest");
    final long orderId = Long.parseLong(dataTable.get("orderId"));
    final boolean reattempt = Boolean.parseBoolean(dataTable.getOrDefault("reattempt", "true"));

    final RescheduleOrderRequest request = JsonUtils.fromJsonSnakeCase(rescheduleRequest,
        RescheduleOrderRequest.class);

    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> {
          final RescheduleOrderResponse response = getOrderClient().rescheduleOrder(orderId,
              request);
          if (reattempt && !response.getStatus().equalsIgnoreCase("Success")) {
            throw new NvTestRuntimeException("reschedule fail: " + response.getMessage());
          }
        }, "Reschedule order", 3000, 10);

  }

  /**
   * @param dataTableRaw <br/> <b>orderId</b>: order ID of the order/parcel e.g.
   *                     {KEY_LIST_OF_CREATED_ORDERS[1].id}<br/>
   *                     <b>rtsRequest</b> { "reason": "Return to sender: Nobody at address",
   *                     "timewindow_id":1, "date":"{date: 1 days next, yyyy-MM-dd}"}<br/>
   */
  @When("API Core - Operator rts order:")
  public void apiOperatorRtsOrder(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final long orderId = Long.parseLong(dataTable.get("orderId"));
    final String rtsRequestString = dataTable.get("rtsRequest");
    final RtsOrderRequest rtsRequest = fromJsonSnakeCase(rtsRequestString, RtsOrderRequest.class);

    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getOrderClient().setReturnedToSender(orderId, rtsRequest), "set RTS order");
  }
}
