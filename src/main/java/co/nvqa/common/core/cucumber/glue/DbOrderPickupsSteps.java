package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.exception.NvTestCoreOrderKafkaLagException;
import co.nvqa.common.core.hibernate.OrderPickupsDao;
import co.nvqa.common.core.model.persisted_class.core.OrderPickup;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class DbOrderPickupsSteps extends CoreStandardSteps {

  @Inject
  private OrderPickupsDao orderPickupsDao;


  /**
   * This method enforce the pickup to be non NULL in order_pickups table
   *
   * @param orderIdString KEY contains order id
   */
  @When("DB Core - get reservation id from order id {string}")
  public void getReservationIdFromOrderId(String orderIdString) {
    final long orderId = Long.parseLong(resolveValue(orderIdString));
    final List<OrderPickup> orderPickups = doWithRetry(() -> {
      final List<OrderPickup> result = orderPickupsDao.getOrderPickupByOrderId(orderId);
      if (result.isEmpty()) {
        throw new NvTestCoreOrderKafkaLagException("pickup is not found for order " + orderId);
      }
      return result;
    }, "reading pickup from order id: " + orderId, 2000, 30);
    // the delay is 2sec * 30 = 1 min, because core sometimes is very slow
    putAllInList(KEY_LIST_OF_RESERVATION_IDS,
        orderPickups.stream()
            .map(OrderPickup::getReservationId)
            .collect(Collectors.toList()));
  }

  @When("DB Core - get Order Pickup Data from order id {string}")
  public void getOrderPickupDataFromOrderId(String orderIdString) {
    final long orderId = Long.parseLong(resolveValue(orderIdString));
    final List<OrderPickup> orderPickups = doWithRetry(() -> {
      final List<OrderPickup> result = orderPickupsDao.getOrderPickupByOrderId(orderId);
      if (result.isEmpty()) {
        throw new NvTestCoreOrderKafkaLagException("pickup is not found for order " + orderId);
      }
      return result;
    }, "reading pickup from order id: " + orderId, 2000, 30);
    putAllInList(KEY_CORE_LIST_OF_ORDER_PICKUPS, orderPickups);
  }
}
