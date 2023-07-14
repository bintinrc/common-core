package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.EditDeliveryOrderRequest;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.Dimension;
import co.nvqa.common.core.model.order.RescheduleOrderRequest;
import co.nvqa.common.core.model.order.RescheduleOrderResponse;
import co.nvqa.common.core.model.order.RtsOrderRequest;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
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
   * @param tracking key that contains order's tracking id, example:
   *                 KEY_LIST_OF_CREATED_TRACKING_IDS
   */
  @When("API Core - Operator get order details for tracking order {string}")
  public void apiOperatorGetOrderDetailsForTrackingOrder(String tracking) {
    final String trackingId = resolveValue(tracking);
    final Order order = doWithRetry(
        () -> getOrderClient().searchOrderByTrackingId(trackingId),
        "Order client search order by tracking id", 20_000, 3);

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
   * @param tracking key that contains order's tracking id, example:
   *                 KEY_LIST_OF_CREATED_TRACKING_IDS
   */
  @When("API Core - Operator get order details for previous order {string}")
  public void apiOperatorGetOrderDetailsForPreviousOrder(String tracking) {
    final String trackingId = resolveValue(tracking);
    final Order order = doWithRetry(() -> getOrderClient().searchOrderByTrackingId(trackingId),
        "Order client search order by tracking id", 20_000, 3);

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
      final Order order = doWithRetry(() -> {
        final Order tempOrder = getOrderClient().searchOrderByTrackingId(trackingId);
        final String actualGranularStatus = tempOrder.getGranularStatus();
        Assertions.assertThat(actualGranularStatus).as("order granular status doesnt match")
            .isEqualToIgnoringCase(expectedGranularStatus);
        return tempOrder;
      }, "Order client search order by tracking id", 30_000, 3);

      putInList(KEY_LIST_OF_CREATED_ORDERS, order,
          (o1, o2) -> StringUtils.equalsAnyIgnoreCase(o1.getTrackingId(), o2.getTrackingId()));
    } catch (NvTestRuntimeException ex) {
      LOGGER.warn(
          "failed to get the order with the expected granular status! cause: " + ex.getMessage());
    }
  }

  /**
   * API Core - Operator update order granular status:
   *
   * @param data <br/> <b>orderId</b>: order id of the order/parcel <br/> <b>granularStatus</b>:
   *             granular status to be updated for the given order
   */
  @Given("API Core - Operator update order granular status:")
  public void apiCoreOperatorUpdateGranularStatusOrder(Map<String, String> data) {
    data = resolveKeyValues(data);
    Long orderId = Long.valueOf(data.get("orderId"));
    String granularStatus = data.get("granularStatus");
    doWithRetry(
        () -> getOrderClient().updateGranularStatusOrder(orderId, granularStatus),
        "update order granular status", 3000, 10);
  }

  /**
   * API Core - Operator Update Delivery Order Details:
   *
   * @param data <br/> <b>orderId</b>: 123456 <br/> <br/> <b>request</b>: { "parcel_job": {
   *             "delivery_date": "2023-06-24", "delivery_timewindow": -1 }, "to": { "name":
   *             "sfksdjldsf", "email": "quix@ninjavan.co", "phone_number": "+6289517318260",
   *             "address": { "address1": "Jonas Street", "address2": "DKI Jakarta, Kota Jakarta
   *             Selatan, Kebayoran Lama", "postcode": "16514", "city": "Jakarta", "country": "ID",
   *             "district": "DKI Jakarta" } } } <br/>
   */
  @Given("API Core - Operator update delivery order details:")
  public void apiCoreOperatorUpdateDeliveryOrderDetails(Map<String, String> data) {
    data = resolveKeyValues(data);
    EditDeliveryOrderRequest request = JsonUtils
        .fromJsonSnakeCase(resolveValue(data.get("request")),
            EditDeliveryOrderRequest.class);
    Long orderId = Long.parseLong(resolveValue(data.get("orderId")));
    doWithRetry(
        () -> getOrderClient().editDeliveryOrderDetails(request, orderId),
        "update order granular status", 3000, 10);
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
    doWithRetry(
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

    doWithRetry(() -> {
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

    doWithRetry(
        () -> getOrderClient().setReturnedToSender(orderId, rtsRequest), "set RTS order");
  }

  @Then("API Core - Operator update order pricing_weight using order-weight-update with data below:")
  public void apiOperatorUpdateOrderPricing_weightToUsingOrderWeightUpdate(
      Map<String, String> data) {
    Map<String, String> resolvedMap = resolveKeyValues(data);
    final long orderId = Long.parseLong(resolvedMap.get("orderId"));
    final double weight = Double.parseDouble(resolvedMap.get("weight"));
    put(KEY_SAVED_ORDER_WEIGHT, weight);
    doWithRetry(() -> getOrderClient().updateOrderPricingWeight(orderId, weight),
        "update order pricing weight using Order Weight Update ");
  }

  @Given("API Core - cancel order and check error:")
  public void apiOperatorCancelCreatedOrderAndGetError(Map<String, String> data) {
    data = resolveKeyValues(data);
    int statusCode = Integer.parseInt(data.getOrDefault("statusCode", "500"));
    String orderIdStr = data.get("orderId");
    if (StringUtils.isBlank(orderIdStr)) {
      throw new IllegalArgumentException("orderId was not defined");
    }
    long orderId = Long.parseLong(orderIdStr);

    String message = data.get("message");
    doWithRetry(() -> {
      Response response = getOrderClient()
          .cancelOrderV1AndGetRawResponse(orderId, "cancelled for testing purposes");
      response.then()
          .statusCode(statusCode)
          .body("messages", Matchers.hasItem(message),
              "data.message", Matchers.equalTo(message));
    }, "try to cancel order and check error", 1000, 5);
  }

  @Given("API Core - cancel order {value}")
  public void apiOperatorCancelCreatedOrder(String orderIdStr) {
    long orderId = Long.parseLong(orderIdStr);
    doWithRetry(() ->
            getOrderClient().cancelOrder(orderId),
        "cancel order");
  }

  @Given("API Core - force cancel order {value}")
  public void apiOperatorForceCancelCreatedOrder(String orderIdStr) {
    long orderId = Long.parseLong(orderIdStr);
    doWithRetry(() ->
            getOrderClient().forceCancelOrder(orderId),
        "force cancel order");
  }

  @Given("API Core - resume order {value}")
  public void apiOperatorResumeTheCreatedOrder(String trackingId) {
    doWithRetry(() -> getOrderClient().resumeOrder(trackingId),
        "resume cancelled order");
  }

  /**
   * @param dataTableRaw <br/> <b>orderId</b>: {KEY_LIST_OF_CREATED_ORDERS[1].id}
   */
  @When("API Core - Operator revert rts order:")
  public void apiOperatorRevertRtsOrder(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final Long orderId = Long.parseLong(dataTable.get("orderId"));
    doWithRetry(
        () -> getOrderClient().revertRts(new RtsOrderRequest(), orderId), "Revert RTS order"
    );
  }

  @Given("API Core - update order dimensions:")
  public void apiOperatorUpdateDimensionsOfAnOrder(Map<String, String> data) {
    data = resolveKeyValues(data);
    String orderId = data.get("orderId");
    Assertions.assertThat(orderId).as("Order ID").isNotNull();
    String dimensionsJson = data.get("dimensions");
    Assertions.assertThat(dimensionsJson).as("Dimensions").isNotNull();
    doWithRetry(() -> getOrderClient().updateParcelDimensions(Long.parseLong(orderId),
        new Dimension(dimensionsJson)), "Update Order Dimensions");
  }

  @When("API Core - Operator bulk tags parcel with below tag:")
  public void apiCoreBulkTagsParcelsWithBelowTag(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final Long orderId = Long.valueOf(dataTable.get("orderId"));
    Long tag = Long.valueOf(dataTable.get("orderTag"));
    List<Long> tags = Collections.singletonList(tag);
    doWithRetry(() -> getOrderClient().addOrderLevelTags(orderId, tags),
        "Operator bulk tags parcel with below tag");
  }


  /**
   * @param dataTableRaw <br/> <b>orderId</b>: {KEY_LIST_OF_CREATED_ORDERS[1].id} <br>
   *                     <b>newCodAmount</b>: 100
   */
  @When("API Core - Operator add/update order COD amount:")
  public void apiCoreUpdateOrderCodAmount(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final Long orderId = Long.valueOf(dataTable.get("orderId"));
    final Double newCodAmount = Double.valueOf(dataTable.get("newCodAmount"));
    doWithRetry(() -> getOrderClient().updateOrderCod(orderId, newCodAmount),
        "Operator add/update order COD amount:");
  }

  /**
   * @param dataTableRaw <br/> <b>orderId</b>: {KEY_LIST_OF_CREATED_ORDERS[1].id} <br/>
   *                     <b>copAmount</b>: 1
   */
  @When("API Core - Operator update order COP:")
  public void apiCoreUpdateOrderCOP(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final Double copAmount = Double.parseDouble(dataTable.get("copAmount"));
    final Long orderId = Long.parseLong(dataTable.get("orderId"));
    doWithRetry(() -> getOrderClient().updateOrderCop(orderId, copAmount),
        "API Core - Operator update order COP");
  }

  /**
   * @param dataTableRaw <br><b>trackingId:</b>
   *                     {KEY_LIST_OF_CREATED_TRACKING_IDS[1]}<br><b>mode:</b> OTP | NONE | AGE |
   *                     SIGNATURE | IDENTIFICATION<br><i>Choose one of the delivery verification
   *                     mode</i>
   */
  @When("API Core - Update order delivery verification mode:")
  public void apiCoreUpdateOrderDeliveryVerification(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final String trackingId = dataTable.get("trackingId");
    final String mode = dataTable.get("mode");
    doWithRetry(() -> getOrderClient().editDeliveryVerificationRequired(trackingId, mode),
        "API Core - Update order delivery verification mode");
  }

  /**
   * @param dataTable <br><b>orderId:</b>
   *                  {KEY_LIST_OF_CREATED_ORDERS[1].id}<br><b>priorityLevel:</b>
   *                  positive_integer_number<br>
   */
  @Given("API Core - Update priority level of an order:")
  public void apiOperatorUpdatePriorityLevelOfAnOrderTo(Map<String, String> dataTable) {
    final Map<String, String> dataTableAsMap = resolveKeyValues(dataTable);
    final Long orderId = Long.parseLong(dataTableAsMap.get("orderId"));
    final Long priorityLevel = Long.parseLong(dataTableAsMap.get("priorityLevel"));
    doWithRetry(() -> {
      getOrderClient().updatePriorityLevelOfTransaction(orderId, priorityLevel.intValue());
    }, "API Core - Update priority level of an order");
  }

  @Given("API Core -  Wait for following order state:")
  public void apiOperatorWaitForOrderStatus(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    Order expectedState = new Order();
    expectedState.fromMap(dataTable);
    int timeout = Integer.parseInt(dataTable.getOrDefault("timeout", "30"));
    Assertions.assertThat(getOrderClient().waitUntilOrderState(expectedState, timeout, 1000))
        .as("Order " + expectedState.getTrackingId() + " didn't get expected state " + dataTable)
        .isTrue();
  }

  @Given("API Core - Verifies order state:")
  public void apiOperatorVerifiesOrderState(Map<String, String> dataTableRaw) {
    Map<String, String> dataTable = new HashMap<>(dataTableRaw);
    dataTable.put("timeout", "1");
    apiOperatorWaitForOrderStatus(dataTable);
  }

  @Given("API Core - wait for order state:")
  public void waitForOrderState(Map<String, String> data) {
    var expected = new Order(resolveKeyValues(data));
    orderClient.waitUntilOrderState(expected, 5 * 60, 5000);
  }
}
