package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.CodInboundsClient;
import co.nvqa.common.core.client.Lazada3PLClient;
import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.exception.NvTestCoreOrderKafkaLagException;
import co.nvqa.common.core.exception.NvTestCoreRescheduleFailedException;
import co.nvqa.common.core.model.CodInbound;
import co.nvqa.common.core.model.EditDeliveryOrderRequest;
import co.nvqa.common.core.model.Lazada3PL;
import co.nvqa.common.core.model.order.BulkForceSuccessOrderRequest;
import co.nvqa.common.core.model.order.DeliveryDetails;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.Dimension;
import co.nvqa.common.core.model.order.OrderTag;
import co.nvqa.common.core.model.order.ParcelJob;
import co.nvqa.common.core.model.order.PricingDetails;
import co.nvqa.common.core.model.order.RescheduleOrderRequest;
import co.nvqa.common.core.model.order.RescheduleOrderResponse;
import co.nvqa.common.core.model.order.RtsOrderRequest;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.StandardTestUtils;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Strings;
import org.hamcrest.Matchers;

@ScenarioScoped
public class ApiOrderSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private OrderClient orderClient;
  @Inject
  @Getter
  private Lazada3PLClient lazada3PLClient;
  @Inject
  @Getter
  private CodInboundsClient codInboundsClient;


  /**
   * <br/><b>Output key</b>: <ul><li>KEY_LIST_OF_CREATED_ORDERS: list of orders</li></ul>
   * <p>
   * read the order state and put it into the scenario manager in list form, it replace the previous
   * order with the same tracking id. <br/><br/><b>Note</b>: becareful that you may face unintended
   * order status due to event propagation delay to Core service
   *
   * @param tracking key that contains order's tracking id, example:
   *                 KEY_LIST_OF_CREATED_TRACKING_IDS
   * @throws IllegalArgumentException when the passed {trackingId} is null or empty
   */
  @When("API Core - Operator get order details for tracking order {string}")
  public void apiOperatorGetOrderDetailsForTrackingOrder(String tracking) {
    final String trackingId = resolveValue(tracking);
    if (Strings.isNullOrEmpty(trackingId)) {
      throw new IllegalArgumentException("Tracking id must not be null");
    }
    final Order order = doWithRetry(
        () -> getOrderClient().searchOrderByTrackingId(trackingId),
        "Order client search order by tracking id", 20_000, 3);

    putInList(KEY_LIST_OF_CREATED_ORDERS, order,
        (o1, o2) -> StringUtils.equalsAnyIgnoreCase(o1.getTrackingId(), o2.getTrackingId()));
  }

  @When("API Core - Operator get multiple order details for tracking ids:")
  public void apiOperatorGetMultipleOrderDetails(List<String> trackingIds) {
    trackingIds.forEach(this::apiOperatorGetOrderDetailsForTrackingOrder);
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
   *
   * @throws IllegalArgumentException when the passed {trackingId} is null or empty
   */
  @When("API Core - Operator get order details for tracking order {string} with granular status {string}")
  public void apiOperatorGetOrderDetailsForTrackingOrderWithGranularStatus(String tracking,
      String expectedGranularStatus) {
    final String trackingId = resolveValue(tracking);
    if (Strings.isNullOrEmpty(trackingId)) {
      throw new IllegalArgumentException("Tracking id must not be null");
    }
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
    } catch (Throwable t) {
      throw new NvTestCoreOrderKafkaLagException(
          "Granular status not updated yet because of Kafka lag");
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
   * API Core - Operator update parcel size:
   *
   * @param data <br/> <b>orderId</b>: 123456 <br/> <br/> <b>size</b>: M <br/>
   */
  @Given("API Core - Operator update parcel size:")
  public void apiCoreOperatorUpdateParcelSize(Map<String, String> data) {
    data = resolveKeyValues(data);

    Dimension dimension = new Dimension();
    dimension.setParcelSize(data.get("size"));

    ParcelJob parcelJob = new ParcelJob();
    parcelJob.setDimensions(dimension);

    DeliveryDetails request = new DeliveryDetails();
    request.setParcelJob(parcelJob);

    Long orderId = Long.parseLong(resolveValue(data.get("orderId")));
    doWithRetry(
        () -> getOrderClient().editDeliveryDetails(orderId, request),
        "edit order details - parcel size", 3000, 10);
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
   * API Core - Operator bulk force success all orders with cod collected "false"
   *
   * @param codCollected example: false
   */
  @When("API Core - Operator bulk force success below orders with cod collected {string}:")
  public void apiOperatorBulkForceSuccessAllOrders(String codCollected, List<String> orderIds) {
    final List<String> finalOrderIds = resolveValues(orderIds);
    List<BulkForceSuccessOrderRequest> request = new ArrayList<>();
    finalOrderIds.stream().distinct().forEach(e -> {
      BulkForceSuccessOrderRequest forceSuccessRequest = new BulkForceSuccessOrderRequest();
      forceSuccessRequest.setOrderId(Long.parseLong(e));
      forceSuccessRequest.setIsCodCollected(Boolean.valueOf(codCollected));
      forceSuccessRequest.setReason("QA AUTO TEST BULK FORCE SUCCESS");
      request.add(forceSuccessRequest);
    });
    getOrderClient().bulkForceSuccess(request);
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
        throw new NvTestCoreRescheduleFailedException("reschedule fail: " + response.getMessage());
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
  public void apiOperatorUpdateOrderPricingWeightToUsingOrderWeightUpdate(
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
    long orderId = Long.parseLong(resolveValue(orderIdStr));
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
   * @param orderId         <br/>orderId: {KEY_LIST_OF_CREATED_ORDERS[1].id}
   * @param expectedTagList :PRIOR
   */
  @When("API Core - Operator check order {string} have the following Tags:")
  public void apiCoreBulkTagsParcelsWithBelowTag(String orderId, List<String> expectedTagList) {
    String resolvedOrderId = resolveValue(orderId);
    List<String> expectedTagsList = expectedTagList.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    final List<String> responseTagList = getOrderClient().getOrderLevelTags(
        Long.parseLong(resolvedOrderId));
    if (!expectedTagsList.isEmpty()) {
      for (String expectedTag : expectedTagsList) {
        boolean contains = responseTagList.contains(expectedTag);
        Assertions.assertThat(contains)
            .as(f("Tags %s exist in order %s", expectedTag, resolvedOrderId))
            .isTrue();
      }
    } else {
      Assertions.assertThat(responseTagList.isEmpty()).as("Tag is empty").isTrue();
    }
  }

  /**
   * @param dataTableRaw <br/> <b>orderId</b>: {KEY_LIST_OF_CREATED_ORDERS[1].id} <br>
   *                     <b>newCodAmount</b>: 100
   */
  @When("API Core - Operator add or update order COD amount:")
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
    doWithRetry(() ->
            getOrderClient().updatePriorityLevelOfTransaction(orderId, priorityLevel.intValue())
        , "API Core - Update priority level of an order");
  }

  /**
   * @param dataTableAsMap <br><b>trackingId:</b>
   *                       {KEY_LIST_OF_CREATED_TRACKING_IDS[1]}<br><b>comment:</b> test comment
   */
  @And("API Core - Operator post Lazada 3PL using data below:")
  public void apiCoreOperatorPostLazada3PL(Map<String, String> dataTableAsMap) {
    dataTableAsMap = resolveKeyValues(dataTableAsMap);
    String trackingId = dataTableAsMap.get("trackingId");
    String comment = dataTableAsMap.get("comment");
    put(KEY_CORE_LAZADA_3PL_COMMENT, comment);
    doWithRetry(() ->
            getLazada3PLClient().postLazada3PL(
                Lazada3PL.builder().comment(comment).trackingId(trackingId).build())
        , "API Core - Operator post Lazada 3PL");
  }

  /**
   * <p>Add tracking id : {KEY_LIST_OF_DASH_ORDERS[1].trackingId} for the created order to 3PL</p>
   * <p>Usage:   And API Core - Add order tracking id to 3PL with the following info:
   * | trackingId           | {KEY_LIST_OF_DASH_ORDERS[1].trackingId} | | thirdPartyTrackingId |
   * {third-party-tracking-id}               | | thirdPartyShipperId  | {third-party-shipper-id}
   * |</p>
   *
   * @param dataTableRaw
   */
  @When("API Core - Add order tracking id to 3PL with the following info:")
  public void apiCoreAddOrderTrackingIdTo3PLWithTheFollowingInfo(Map<String, String> dataTableRaw) {
    String trackingId = resolveValue(dataTableRaw.get("trackingId"));
    String thirdPartyTrackingId = resolveValue(dataTableRaw.get("thirdPartyTrackingId"));
    Long thirdPartyShipperId = Long.parseLong(
        resolveValue(dataTableRaw.get("thirdPartyShipperId")));
    orderClient.addOrderTo3pl(trackingId, thirdPartyTrackingId, thirdPartyShipperId);
  }

  @Given("API Core -  Wait for following order state:")
  public void apiOperatorWaitForOrderStatus(Map<String, String> dataTableRaw) {
    try {
      final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
      Order expectedState = new Order();
      expectedState.fromMap(dataTable);
      int timeout = Integer.parseInt(dataTable.getOrDefault("timeout", "30"));
      Assertions.assertThat(getOrderClient().waitUntilOrderState(expectedState, timeout, 1000))
          .as("Order " + expectedState.getTrackingId() + " didn't get expected state " + dataTable)
          .isTrue();
    } catch (Throwable t) {
      throw new NvTestCoreOrderKafkaLagException(
          "order state not updated yet because of Kafka lag ");
    }
  }

  @Given("API Core - Verifies order state:")
  public void apiOperatorVerifiesOrderState(Map<String, String> dataTableRaw) {
    Map<String, String> dataTable = new HashMap<>(dataTableRaw);
    dataTable.put("timeout", "1");
    apiOperatorWaitForOrderStatus(dataTable);
  }

  @Given("API Core - wait for order state:")
  public void waitForOrderState(Map<String, String> data) throws InterruptedException {
    var expected = new Order(resolveKeyValues(data));
    orderClient.waitUntilOrderState(expected, 5 * 60, 5000);
  }

  @Given("API Core - Operator delete order with order id {string}")
  public void apiDeleteOrderWithOrderId(String orderIdStr) {
    doWithRetry(() -> orderClient.deleteOrder(Long.parseLong(resolveValue(orderIdStr))),
        "API Core - Operator delete order with order id");
  }


  /**
   * @param dataTable <br><b>orderId:</b>
   *                  KEY_LIST_OF_CREATED_ORDERS[1].id<br><b>tagId:</b> the id of the tag to be
   *                  deleted<br>
   */
  @Given("API Core - Operator delete tag from order:")
  public void apiDeleteTagFromOrder(Map<String, String> dataTable) {
    dataTable = resolveKeyValues(dataTable);
    Long orderId = Long.valueOf(dataTable.get("orderId"));
    Integer tagId = Integer.valueOf(dataTable.get("tagId"));
    List<Integer> tagIdList = new ArrayList<>();
    tagIdList.add(tagId);
    Map<String, Object> payLoad = new HashMap<>();
    payLoad.put("order_id", orderId);
    payLoad.put("tags", tagIdList);
    doWithRetry(() -> orderClient.deleteTagFromOrder(payLoad),
        "API Core - Operator delete tag from order:");
  }

  @When("API Core - Operator create new COD Inbound for created order:")
  public void operatorCreateNewCod(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    final Long routeId = Long.parseLong(resolvedData.get("routeId"));
    final Double codGoodsAmount = Double.parseDouble(resolvedData.get("codAmount"));
    Assertions.assertThat(codGoodsAmount).as("COD Goods Amount should not be null.").isNotNull();

    Double amountCollectedNotRounded = codGoodsAmount - (codGoodsAmount / 2);
    BigDecimal amountCollectedBigDecimal = BigDecimal.valueOf(amountCollectedNotRounded)
        .setScale(2, RoundingMode.HALF_UP);
    Double amountCollected = amountCollectedBigDecimal.doubleValue();

    String receiptNumber = "#" + routeId + "-" + StandardTestUtils.generateDateUniqueString();

    CodInbound codInbound = new CodInbound();
    codInbound.setRouteId(routeId);
    codInbound.setAmountCollected(amountCollected);
    codInbound.setReceiptNo(receiptNumber);

    getCodInboundsClient().codInbound(codInbound);

    put(KEY_CORE_ROUTE_CASH_INBOUND_COD, codInbound);
  }

  /**
   * @param dataTable <br><b>trackingId:</b>
   *                  {KEY_LIST_OF_CREATED_TRACKING_IDS[1]}<br><b>eventType:</b> Weight Updated
   */
  @Given("API Core - Operator recalculate order price:")
  public void apiOperatorRecalculateOrderPriceWithEvenType(Map<String, String> dataTable) {
    dataTable = resolveKeyValues(dataTable);
    String trackingId = dataTable.get("trackingId");
    String eventType = dataTable.get("eventType");

    doWithRetry(
        () -> getOrderClient().batchRecalculate(eventType, trackingId),
        "batch recalculate order", 3000, 10);
  }

  /**
   * Cancel the Order by passing Tracking ID
   *
   * @param orderTrackingId key that contains order's tracking id, example:
   *                        KEY_LIST_OF_CREATED_TRACKING_IDS
   */
  @Given("API Core - cancel order using tracking id {value} by using shipper token: {value}")
  public void apiOperatorCancelCreatedOrderUsingTrackingId(String orderTrackingId,
      String shipperToken) {
    doWithRetry(() ->
            new OrderClient(resolveValue(shipperToken)).cancelOrderV3(orderTrackingId),
        "cancel order using tracking id");
  }

  @Given("API Core - Login with clientId {string} and clientSecret {string}")
  public void apiShipperLoginWithCredentials(String clientId, String secret) {
    String token = TokenUtils.getShipperToken(clientId, secret);
    put(KEY_CORE_SHIPPER_TOKEN, token);
  }

  @Then("API Core - verify order pricing details:")
  public void apiOperatorVerifyPricingInfo(Map<String, String> data) {
    PricingDetails expected = new PricingDetails(resolveKeyValues(data));
    PricingDetails actual = getOrderClient().getPricingDetails(expected.getOrderId());
    expected.compareWithActual(actual);
  }

  /**
   * @param data <br><b>name:</b>
   *             ABF<br><b>description:</b> This tag is created by Automation Test
   */
  @Given("API Core - create new order tag:")
  public void apiOperatorCreateOrderTag(Map<String, String> data) {
    final Map<String, String> dataTable = resolveKeyValues(data);
    final String json = toJsonSnakeCase(dataTable);
    final OrderTag request = fromJsonSnakeCase(json, OrderTag.class);
    doWithRetry(() -> {
      OrderTag response = getOrderClient().createOrderTag(request);
      putInList(KEY_CORE_LIST_OF_CREATED_ORDER_TAGS, response);
    }, "create tag", 1000, 5);
  }

  @Given("API Core - delete {string} order tag")
  public void apiDeleteOrderTag(String tagId) {
    doWithRetry(() -> orderClient.deleteOrderTag(Long.valueOf(resolveValue(tagId))),
        "API Core - Operator delete order tag with tag id");
  }

  /**
   * @param dataTableRaw <br/> <b>orderId</b>: {KEY_LIST_OF_CREATED_ORDERS[1].id}<br/>
   *                     <b>deliveryType</b>: 6 <br/>
   */
  @Given("API Core - update order delivery type:")
  public void apiOperatorUpdateOrderDeliveryTypeTo(Map<String, String> dataTableRaw) {
    final Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    final long orderId = Long.parseLong(dataTable.get("orderId"));
    final int deliveryType = Integer.parseInt(dataTable.get("deliveryType"));
    doWithRetry(
        () -> getOrderClient().updateDeliveryTypes(orderId, deliveryType),
        "Update order delivery type");
      }

}