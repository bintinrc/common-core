package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.utils.CoreScenarioStorageKeys;
import co.nvqa.common.cucumber.StandardScenarioManager;
import co.nvqa.common.cucumber.glue.StandardSteps;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.java.en.When;

public class StandardApiOrderSteps extends StandardSteps<StandardScenarioManager> implements
    CoreScenarioStorageKeys {

  private OrderClient orderClient;

  @Override
  public void init() {

  }

  public OrderClient getOrderClient() {
    if (orderClient == null) {
      orderClient = new OrderClient(StandardTestConstants.API_BASE_URL,
          TokenUtils.getOperatorAuthToken());
    }

    return orderClient;
  }

  @When("API Operator get order details for tracking order {string}")
  public void apiOperatorCreateV2ReservationUsingDataBelow(String tracking) {
    String trackingId = resolveValue(tracking);
    Order order = retryIfAssertionErrorOccurred(
        () -> getOrderClient().searchOrderByTrackingId(trackingId),
        "Order client search order by tracking id");

    putInList(KEY_LIST_OF_CREATED_ORDERS, order);
  }
}
