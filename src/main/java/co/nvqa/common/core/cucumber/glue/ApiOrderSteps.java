package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.NvTestRuntimeException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;

public class ApiOrderSteps extends CoreStandardSteps {

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
   * @param tracking key that contains order's tracking id, example:
   *                 KEY_LIST_OF_CREATED_TRACKING_IDS
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

  private OrderClient getOrderClient() {
    if (orderClient == null) {
      orderClient = new OrderClient(StandardTestConstants.API_BASE_URL,
          TokenUtils.getOperatorAuthToken());
    }

    return orderClient;
  }
}
