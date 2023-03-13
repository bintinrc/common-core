package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.Transaction;
import com.google.common.collect.Iterables;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class OrderVerificationSteps extends CoreStandardSteps {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderSteps.class);

  @Inject
  @Getter
  private OrderClient orderClient;

  @Override
  public void init() {

  }

  //  type = Pickup|Delivery
  @Given("API Core - Operator verifies {string} transactions of following orders have same waypoint id:")
  public void verifySameWaypointId(String type, List<String> orderIds) {
    orderIds = resolveValues(orderIds);
    Map<String, Long> waypointIds = orderIds.stream().collect(Collectors.toMap(
        orderId -> orderId,
        orderId -> {
          Order order = apiOperatorGetOrderDetails(Long.parseLong(orderId));
          Transaction transaction =
              Transaction.TYPE_PICKUP.equalsIgnoreCase(type) ? getLastTransactionWithType(order,
                  Transaction.TYPE_PICKUP)
                  : getLastTransactionWithType(order, Transaction.TYPE_DELIVERY);
          Long waypointId = transaction.getWaypointId();
          Assertions.assertThat(waypointId).as("waypoint id not null").isNotNull();
          return waypointId;
        }
    ));

    if (waypointIds.values().stream().distinct().count() != 1) {
      Assertions.fail(
          f("Not all %s waypoint ids of given orders are the same: %s", waypointIds));
    }

  }

  //  type = Pickup|Delivery
  @Given("API Core - Operator verifies {string} transactions of following orders have different waypoint id:")
  public void verifyDifferentWaypointId(String type, List<String> orderIds) {
    orderIds = resolveValues(orderIds);
    Map<String, Long> waypointIds = orderIds.stream().collect(Collectors.toMap(
        orderId -> orderId,
        orderId -> {
          Order order = apiOperatorGetOrderDetails(Long.parseLong(orderId));
          Transaction transaction =
              Transaction.TYPE_PICKUP.equalsIgnoreCase(type) ? getLastTransactionWithType(order,
                  Transaction.TYPE_PICKUP)
                  : getLastTransactionWithType(order, Transaction.TYPE_DELIVERY);
          Long waypointId = transaction.getWaypointId();
          Assertions.assertThat(waypointId).as("waypoint id not null").isNotNull();
          return waypointId;
        }
    ));

    if (waypointIds.values().stream().distinct().count() != orderIds.size()) {
      Assertions.fail(
          f("Not all %s waypoint ids of given orders are the different: %s", waypointIds));
    }
  }


  private Order apiOperatorGetOrderDetails(Long orderId) {
    String methodInfo = f("%s - [Order ID = %d]", getCurrentMethodName(), orderId);
    return retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> getOrderClient().getOrder(orderId),
        methodInfo);
  }

  private Transaction getLastTransactionWithType(Order order, String type) {
    String trackingId = order.getTrackingId();
    List<Transaction> transactions = order.getTransactions();

    if (CollectionUtils.isEmpty(transactions)) {
      Assertions.fail(f("Transactions not found for tracking ID '%s'.", trackingId));
    }

    List<Transaction> result = transactions.stream().filter(
        transaction -> StringUtils.equalsIgnoreCase(type, transaction.getType()))
        .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(result)) {
      Assertions.fail(f("%s transactions not found for tracking ID '%s'.", type, trackingId));
    }

    return Iterables.getLast(result);
  }

}
