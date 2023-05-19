package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.OrderDao;
import co.nvqa.common.core.model.order.Order.Data;
import co.nvqa.common.core.model.order.Order.Dimension;
import co.nvqa.common.core.model.order.Order.PreviousAddressDetails;
import co.nvqa.common.core.model.persisted_class.core.Orders;
import co.nvqa.common.utils.NvTestRuntimeException;
import co.nvqa.common.utils.StandardTestUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.DoubleStream;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;


public class DBOrdersTableSteps extends CoreStandardSteps {

  @Inject
  private OrderDao orderDao;

  @Override
  public void init() {
  }

  @Given("DB Core - verify order weight updated to highest weight within range")
  public void dbOperatorVerifiesHighestOrderWeight(Map<String, String> source) {
    Map<String, String> expectedData = resolveKeyValues(source);
    final long orderId = Long.parseLong(expectedData.get("order_id"));
    final double expectedWeight = Double.parseDouble(source.get("weight"));
    if (Boolean.parseBoolean(expectedData.get("use_weight_range"))) {
      dbOperatorVerifiesOrderWeightRangeUpdated(expectedData);
    } else {
      doWithRetry(() -> {
        double actualWeight = orderDao.getOrderWeight(orderId);
        Assertions.assertThat(actualWeight).as("orders.weight equals highest weight")
            .isEqualTo(expectedWeight);
        put(KEY_SAVED_ORDER_WEIGHT, actualWeight);
      }, f("Get orders.weight of id %s ", orderId), 10_000, 3);
    }
  }

  @Given("DB Core - verify order weight range updated correctly")
  public void dbOperatorVerifiesOrderWeightRangeUpdated(Map<String, String> source) {

    Map<String, String> expectedData = resolveKeyValues(source);
    final long orderId = Long.parseLong(expectedData.get("order_id"));
    final double expectedWeight = Double.parseDouble(source.get("weight"));
    final Double higherBound = expectedWeight;
    final Double lowerBound = expectedWeight - 0.5;

    doWithRetry(() -> {
      double actualWeight = orderDao.getOrderWeight(orderId);
      Assertions.assertThat(actualWeight >= lowerBound)
          .as("Order weight should be greater than " + expectedWeight + " - 0.5").isTrue();
      Assertions.assertThat(actualWeight <= higherBound)
          .as("Order weight should be lover than " + expectedWeight + " - 0").isTrue();
      put(KEY_SAVED_ORDER_WEIGHT, actualWeight);
    }, f("get orders weight of order id %s", orderId), 10_000, 3);
  }

  @Given("DB Core - verify orders.weight and dimensions updated correctly for order id {string}")
  public void dbOperatorVerifiesOrdersWeightDims(String id, Map<String, String> source) {
    Long orderId = Long.parseLong(resolveValue(id));
    doWithRetry(() -> {
      if (source.containsKey("orders.weight")) {
        final Double actualOrdersWeight = orderDao.getOrderWeight(orderId);
        Assertions.assertThat(actualOrdersWeight).as("orders.weight equal")
            .isEqualTo(Double.parseDouble(source.get("orders.weight")), Assertions.offset(0.1));
      }
      if (source.containsKey("orders.dimensions.weight")) {
        final Double actualOrdersDimWeight = orderDao.getOrderDimensions(orderId).getWeight();
        Assertions.assertThat(actualOrdersDimWeight).as("orders.dimensions.weight equal")
            .isEqualTo(Double.parseDouble(source.get("orders.dimensions.weight")),
                Assertions.offset(0.1));
      }
      if (source.containsKey("orders.data.originalWeight")) {
        final Double actualOrdersDataOriginalWeight = orderDao.getOrderData(orderId)
            .getOriginalWeight();
        Assertions.assertThat(actualOrdersDataOriginalWeight).as("orders.data.originalWeight equal")
            .isEqualTo(Double.parseDouble(source.get("orders.data.originalWeight")),
                Assertions.offset(0.1));
        put(KEY_EXPECTED_OLD_WEIGHT, actualOrdersDataOriginalWeight);
      }
      if (source.containsKey("orders.data.originalDimensions.weight")) {
        final Double actualOrdersDataOriginalDimWeight = orderDao.getOrderData(orderId)
            .getOriginalDimensions().getWeight();
        Assertions.assertThat(actualOrdersDataOriginalDimWeight)
            .as("orders.data.originalDimensions.weight equal")
            .isEqualTo(Double.parseDouble(source.get("orders.data.originalDimensions.weight")),
                Assertions.offset(0.1));
      }
    }, f("verify orders weight & dimensions of order id %s", orderId), 10_000, 3);
  }

  @Given("DB Core - verify orders.weight is updated to highest weight correctly")
  public void operatorVerifiesUpdatedWeight(Map<String, String> data) {
    Map<String, String> resolvedMap = resolveKeyValues(data);
    final Long orderId = Long.parseLong(resolvedMap.get("orderId"));
    final Double length = Double.parseDouble(resolvedMap.get("length"));
    final Double width = Double.parseDouble(resolvedMap.get("width"));
    final Double height = Double.parseDouble(resolvedMap.get("height"));
    final Double measuredWeight = Double.parseDouble(resolvedMap.get("weight"));
    final Double shipperSubmittedWeight = Double
        .parseDouble(resolvedMap.get("shipperSubmittedWeight"));
    doWithRetry(() -> {
      Double actualWeight = orderDao.getOrderWeight(orderId);
      Assertions.assertThat(actualWeight).as("order weight is not null").isNotNull();
      double volumetricWeight =
          (length * width * height) / 6000;
      double shipperAdjustedWeight =
          (Math.floor(shipperSubmittedWeight / 100)) / 10;
      double expectedWeight = DoubleStream.of(shipperAdjustedWeight, measuredWeight,
          volumetricWeight).max().getAsDouble();

      Assertions.assertThat(actualWeight).as("orders.weight equal highest weight")
          .isEqualTo(expectedWeight);
    }, f("Get orders.weight of id %s ", orderId), 10_000, 3);
  }

  @Then("DB Core - Operator generate stamp id for {string} orders")
  public void generateStampId(String totalOrder) {
    long totalOrderRequest = Long.parseLong(totalOrder);
    for (int i = 0; i < totalOrderRequest; i++) {
      doWithRetry(() -> {
        try {
          String stampId = StandardTestUtils.generateStampId();
          if (orderDao.getSingleOrderDetailsByStampId(stampId) != null) {
            throw new AssertionError();
          }
          putInList(KEY_CORE_LIST_OF_CREATED_STAMP_ID, stampId);
        } catch (AssertionError ae) {
          throw new NvTestRuntimeException(ae);
        }
      }, "Generate Stamp ID");
    }
  }

  @Then("DB Core - Operator get order by stamp id {string}")
  public void getOrderByStampId(String stampId) {
    String stampIdCheck = resolveValue(stampId);
    doWithRetry(() -> {
      Orders order = orderDao.getSingleOrderDetailsByStampId(stampIdCheck);
      putInList(KEY_CORE_LIST_OF_CREATED_ORDERS_CORE_DB, order,
          (o1, o2) -> Objects.equals(o1.getId(), o2.getId()));
    }, "fetch order detail by Stamp Id", 10_000, 3);
  }

  @When("DB Core - verify order id {string} orders.dimensions record:")
  public void verifyOrderDimensionsRecords(String id, Map<String, String> data) {
    final Map<String, String> resolvedData = resolveKeyValues(data);
    final long orderId = Long.parseLong(resolveValue(id));
    Dimension expected = new Dimension(resolvedData);
    doWithRetry(() -> {
      Dimension actual = orderDao.getOrderDimensions(orderId);
      Assertions.assertThat(actual)
          .withFailMessage("orders record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify orders.dimensions records", 10_000, 3);
  }

  @When("DB Core - verify order id {string} orders.data.manual_dimensions record:")
  public void verifyOrderDataManualDimensionsRecords(String id, Map<String, String> data) {
    final Map<String, String> resolvedData = resolveKeyValues(data);
    final long orderId = Long.parseLong(resolveValue(id));
    Dimension expected = new Dimension(resolvedData);
    doWithRetry(() -> {
      Dimension actual = orderDao.getOrderManualDimensions(orderId);
      Assertions.assertThat(actual)
          .withFailMessage("orders record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify orders.data.manual_dimensions records", 10_000, 3);
  }

  @When("DB Core - operator verify orders.data.previousDeliveryDetails is updated correctly:")
  public void verifyOrdersDataPreviousDelivery(Map<String, String> source) {
    Long resolvedOrderId = Long.parseLong(resolveValue(source.get("orderId")));
    Map<String, String> resolvedMap = resolveKeyValues(source);
    doWithRetry(() -> {
      String orderData = orderDao.getSingleOrderDetailsById(resolvedOrderId).getData();
      List<PreviousAddressDetails> previousAddressDetails = fromJsonCamelCase(orderData, Data.class)
          .getPreviousDeliveryDetails();
      PreviousAddressDetails actual = previousAddressDetails.get(previousAddressDetails.size() - 1);
      PreviousAddressDetails expected = new PreviousAddressDetails(resolvedMap);
      Assertions.assertThat(actual)
          .withFailMessage("previous address details not found")
          .isNotNull();
      expected.compareWithActual(actual, resolvedMap);
    }, "verify previousDeliveryDetails", 10_000, 3);
  }

  @When("DB Core - operator verify orders.data.previousPickupDetails is updated correctly:")
  public void verifyOrdersDataPreviousPickup(Map<String, String> source) {
    Long resolvedOrderId = Long.parseLong(resolveValue(source.get("orderId")));
    Map<String, String> resolvedMap = resolveKeyValues(source);
    doWithRetry(() -> {
      String orderData = orderDao.getSingleOrderDetailsById(resolvedOrderId).getData();
      List<PreviousAddressDetails> previousAddressDetails = fromJsonCamelCase(orderData, Data.class)
          .getPreviousPickupDetails();
      PreviousAddressDetails actual = previousAddressDetails.get(previousAddressDetails.size() - 1);
      PreviousAddressDetails expected = new PreviousAddressDetails(resolvedMap);
      Assertions.assertThat(actual)
          .withFailMessage("previous address details not found")
          .isNotNull();
      expected.compareWithActual(actual, resolvedMap);
    }, "verify previousPickupDetails", 10_000, 3);
  }

  @When("DB Core - verify orders record:")
  public void verifyOrderRecords(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Orders expected = new Orders(resolvedData);
    doWithRetry(() -> {
      Orders actual = orderDao.getSingleOrderDetailsById(expected.getId());
      Assertions.assertThat(actual)
          .withFailMessage("orders record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify orders records", 10_000, 3);
  }

}