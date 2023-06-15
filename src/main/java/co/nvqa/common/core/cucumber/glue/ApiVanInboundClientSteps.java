package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.VanInboundClient;
import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.VanInboundRequest;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.Transaction;
import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

public class ApiVanInboundClientSteps extends CoreStandardSteps {

  public static final int MAX_COMMENTS_LENGTH_ON_SHIPPER_PICKUP_PAGE = 255;
  @Inject
  @Getter
  private VanInboundClient vanInboundClient;
  @Inject
  @Getter
  private RouteClient routeClient;

  @Override
  public void init() {

  }

  /**
   * Example: And API Core - Operator van inbound orders
   * |ordersListKey|{KEY_LIST_OF_CREATED_ORDERS[1]}| |routeKey|{KEY_LIST_OF_CREATED_ROUTES[1].id}|
   * @param data Map of data from feature file.
   */

  @Given("API Core - Operator van inbound order:")
  public void apiOperatorVanInbound(Map<String, String> data) {
    String routListKey = data.get("routeListKey");
    String ordersListKey = data.get("ordersListKey");
    List<Order> orders = get(ordersListKey);
    Long routeId = get(routListKey);
    if (CollectionUtils.isEmpty(orders)) {
      throw new IllegalArgumentException(
          "There are no orders stored under [" + ordersListKey + "] list");
    }
    for (Order order : orders) {
      List<Transaction> trx = order.getTransactions();
      String trackingId = order.getTrackingId();
      Long deliveryWaypointId = trx.get(trx.size() - 1).getWaypointId();
      apiOperatorVanInboundParcel(trackingId, deliveryWaypointId, routeId);
    }
  }

  @Given("API Core - Operator van inbound and start route using data:")
  public void apiOperatorVanInboundAndStartRoute(Map<String, String> data) {
    apiOperatorVanInbound(data);
    String routListKey = data.get("routeListKey");
    Long routeId = get(routListKey);
    String methodInfo = f("%s - [Route ID = %d]", getCurrentMethodName(), routeId);
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> getRouteClient().startRoute(routeId),
        methodInfo);
  }

  public void apiOperatorVanInboundParcel(String trackingId, Long deliveryWaypointId,
      Long routeId) {
    String methodInfo = f("%s - [Tracking ID = %s]", getCurrentMethodName(), trackingId);

    retryIfAssertionErrorOrRuntimeExceptionOccurred(() ->
    {
      VanInboundRequest vanInboundRequest = new VanInboundRequest();
      vanInboundRequest.setTrackingId(trackingId);
      vanInboundRequest.setWaypointId(deliveryWaypointId);
      vanInboundRequest.setType("VAN_FROM_NINJAVAN");
      getVanInboundClient().vanInbound(vanInboundRequest, routeId);
    }, methodInfo);
  }

}
