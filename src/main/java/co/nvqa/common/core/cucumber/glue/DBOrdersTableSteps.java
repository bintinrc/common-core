package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.OrderDao;
import co.nvqa.common.core.model.order.Order.Data;
import co.nvqa.common.core.model.order.Order.PreviousAddressDetails;
import co.nvqa.common.core.model.persisted_class.core.Orders;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
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
      retryIfAssertionErrorOccurred(() -> {
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

    retryIfAssertionErrorOccurred(() -> {
      double actualWeight = orderDao.getOrderWeight(orderId);
      Assertions.assertThat(actualWeight >= lowerBound)
          .as("Order weight should be greater than " + expectedWeight + " - 0.5").isTrue();
      Assertions.assertThat(actualWeight <= higherBound)
          .as("Order weight should be lover than " + expectedWeight + " - 0").isTrue();
      put(KEY_SAVED_ORDER_WEIGHT, actualWeight);
    }, f("get orders weight of order id %s", orderId), 10_000, 3);
  }

  @When("DB Core - operator verify orders.data.previousDeliveryDetails is updated correctly:")
  public void verifyOrdersDataPreviousDelivery(Map<String, String> source) {
    Long resolvedOrderId = Long.parseLong(resolveValue(source.get("orderId")));
    Map<String, String> resolvedMap = resolveKeyValues(source);
    retryIfAssertionErrorOccurred(() -> {
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
    retryIfAssertionErrorOccurred(() -> {
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
    retryIfAssertionErrorOccurred(() -> {
      Orders actual = orderDao.getSingleOrderDetailsById(expected.getId());
      Assertions.assertThat(actual)
          .withFailMessage("orders record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify orders records", 10_000, 3);
  }

}
