package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.OrderPickupsDao;
import co.nvqa.common.core.model.persisted_class.OrderPickup;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.stream.Collectors;

public class DbOrderPickupsSteps extends CoreStandardSteps {

  private OrderPickupsDao orderPickupsDao;

  @Override
  public void init() {
    orderPickupsDao = new OrderPickupsDao();
  }

  /**
   * This method enforce the pickup to be non NULL in order_pickups table
   *
   * @param orderIdString KEY contains order id
   */
  @When("DB Core - get reservation id from order id {string}")
  public void getReservationIdFromOrderId(String orderIdString) {
    final long orderId = Long.parseLong(resolveValue(orderIdString));
    final List<OrderPickup> orderPickups = retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      final List<OrderPickup> result = orderPickupsDao.getOrderPickupByOrderId(orderId);
      if (result.isEmpty()) {
        throw new NvTestRuntimeException("pickup is not found for order " + orderId);
      }
      return result;
    }, "reading pickup from order id: " + orderId);
    put(KEY_LIST_OF_RESERVATION_IDS,
        orderPickups.stream()
            .map(OrderPickup::getReservationId)
            .collect(Collectors.toList()));
  }
}
