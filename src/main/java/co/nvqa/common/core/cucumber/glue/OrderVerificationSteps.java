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
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;

@ScenarioScoped
public class OrderVerificationSteps extends CoreStandardSteps {

  public static final String TRANSACTION_TYPE_PICKUP = "PICKUP";
  public static final String TRANSACTION_TYPE_DELIVERY = "DELIVERY";

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

  /**
   * Given API Core - Operator refresh the following orders | {KEY_LIST_OF_CREATED_ORDERS[1].id} |
   */
  @Given("API Core - Operator refresh the following orders")
  public void apiOperatorRefreshCreatedOrderData(List<String> orderIds) {
    doWithRetry(
        () -> orderIds.forEach(
            o -> putInList(KEY_LIST_OF_CREATED_ORDERS,
                apiOperatorGetOrderDetails(resolveValue(o)))),
        "refresh created order");

  }
  private Order apiOperatorGetOrderDetails(Long orderId) {
    String methodInfo = f("%s - [Order ID = %d]", getCurrentMethodName(), orderId);
    return doWithRetry(() -> getOrderClient().getOrder(orderId),
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

  @Given("API Core - verify {word} transaction of the order:")
  public void apiOperatorVerifyTransaction(String transactionType, Map<String, String> data) {
    String ordersListKey = data.get("ordersListKey");
    if (StringUtils.isBlank(ordersListKey)) {
      throw new IllegalArgumentException("ordersListKey was not defined");
    }
    data = resolveKeyValues(data);
    List<Order> orders = get(ordersListKey);
    if (CollectionUtils.isEmpty(orders)) {
      throw new IllegalArgumentException(
          "There are no orders stored under [" + ordersListKey + "] list");
    }
    String orderIdStr = data.get("orderId");
    long orderId =
        StringUtils.isNotBlank(orderIdStr) ? Long.parseLong(orderIdStr)
            : orders.get(orders.size() - 1).getId();
    Order order = orders.stream()
        .filter(o -> o.getId() != null && o.getId() == orderId)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "Data for order [" + orderId + "] was not found in KEY_LIST_OF_CREATED_ORDERS"));
    Transaction transaction;
    switch (transactionType.toUpperCase()) {
      case TRANSACTION_TYPE_PICKUP:
        transaction = order.getLastPickupTransaction();
        break;
      case TRANSACTION_TYPE_DELIVERY:
        transaction = order.getLastDeliveryTransaction();
        break;
      default:
        throw new IllegalArgumentException(f("Unknown transaction type [%s]", transactionType));
    }
    put(KEY_CORE_TRANSACTION, transaction);
    if (data.containsKey("status")) {
      Assertions.assertThat(transaction.getStatus())
          .as(f("%s transaction: status", transactionType)).isEqualTo(data.get("status"));
    }
    if (data.containsKey("comments")) {
      Assertions.assertThat(transaction.getComments())
          .as(f("%s transaction: comments", transactionType)).isEqualTo(data.get("comments"));
    }
  }

  @Given("API Core - save the last {word} transaction of {value} order from {string} as {string}")
  public void saveTransaction(String transactionType, String orderId, String listKey, String key) {
    List<Order> orders = get(listKey);
    if (CollectionUtils.isEmpty(orders)) {
      throw new IllegalArgumentException("There are no orders stored under [" + listKey + "] list");
    }
    Order order = orders.stream()
        .filter(o -> Objects.equals(o.getId(), Long.valueOf(orderId)))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Order with Id " + orderId + " was not found"));
    saveTransaction(transactionType, key, order);
  }

  private void saveTransaction(String transactionType, String key, Order order) {
    Transaction transaction;
    switch (transactionType.toUpperCase()) {
      case TRANSACTION_TYPE_PICKUP:
        transaction = order.getLastPickupTransaction();
        break;
      case TRANSACTION_TYPE_DELIVERY:
        transaction = order.getLastDeliveryTransaction();
        break;
      default:
        throw new IllegalArgumentException(f("Unknown transaction type [%s]", transactionType));
    }
    put(key, transaction);
  }

}
