package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;

public class ApiOrderSteps extends CoreStandardSteps {

  private OrderClient orderClient;

  @Override
  public void init() {

  }

  @When("API Core - Operator get order details for tracking order {string}")
  public void apiOperatorCreateV2ReservationUsingDataBelow(String tracking) {
    String trackingId = resolveValue(tracking);
    Order order = retryIfAssertionErrorOccurred(
        () -> getOrderClient().searchOrderByTrackingId(trackingId),
        "Order client search order by tracking id");

    putInList(KEY_LIST_OF_CREATED_ORDERS, order,
        (o1, o2) -> StringUtils.equalsAnyIgnoreCase(o1.getTrackingId(), o2.getTrackingId()));
  }

  private OrderClient getOrderClient() {
    if (orderClient == null) {
      orderClient = new OrderClient(StandardTestConstants.API_BASE_URL,
          TokenUtils.getOperatorAuthToken());
    }

    return orderClient;
  }
}
