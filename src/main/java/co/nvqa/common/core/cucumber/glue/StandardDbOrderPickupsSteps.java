package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.hibernate.OrderPickupsDao;
import co.nvqa.common.core.model.persisted_class.OrderPickup;
import co.nvqa.common.core.utils.CoreScenarioStorageKeys;
import co.nvqa.common.cucumber.StandardScenarioManager;
import co.nvqa.common.cucumber.glue.StandardSteps;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.stream.Collectors;

public class StandardDbOrderPickupsSteps extends StandardSteps<StandardScenarioManager> implements
    CoreScenarioStorageKeys {

  private OrderPickupsDao orderPickupsDao;

  @Override
  public void init() {
    orderPickupsDao = new OrderPickupsDao();
  }

  @When("DB Core get reservation id from order id {string}")
  public void getReservationIdFromOrderId(String orderIdString) {
    final long orderId = Long.parseLong(resolveValue(orderIdString));
    final List<OrderPickup> orderPickups = orderPickupsDao.getOrderPickupByOrderId(orderId);
    put(KEY_LIST_OF_RESERVATION_IDS,
        orderPickups.stream()
            .map(OrderPickup::getReservationId)
            .collect(Collectors.toList()));
  }
}
