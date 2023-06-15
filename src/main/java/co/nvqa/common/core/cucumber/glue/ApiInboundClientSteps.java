package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.InboundClient;
import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.VanInboundRequest;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.Transaction;
import io.cucumber.java.en.Given;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;

public class ApiInboundClientSteps extends CoreStandardSteps {

  public static final int MAX_COMMENTS_LENGTH_ON_SHIPPER_PICKUP_PAGE = 255;
  @Inject
  @Getter
  private InboundClient inboundClient;
  @Inject
  @Getter
  private RouteClient routeClient;

  @Override
  public void init() {

  }

  @Given("API Core - Operator van inbound")
  public void apiOperatorVanInbound() {
    List<Order> listOfOrders = get(KEY_LIST_OF_CREATED_ORDERS);

    for (Order order : listOfOrders) {
      List<Transaction> trx = order.getTransactions();
      String trackingId = order.getTrackingId();
      Long deliveryWaypointId = trx.get(trx.size() - 1).getWaypointId();

      apiOperatorVanInboundParcel(trackingId, deliveryWaypointId);
    }
  }
  @Given("API Core - Operator van inbound and start route")
  public void apiOperatorVanInboundAndStartRoute() {
    apiOperatorVanInbound();
    Long routeId = get(KEY_CREATED_ROUTE_ID);
    String methodInfo = f("%s - [Route ID = %d]", getCurrentMethodName(), routeId);
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> getRouteClient().startRoute(routeId),
        methodInfo);
  }
  public void apiOperatorVanInboundParcel(String trackingId, Long deliveryWaypointId) {
    String methodInfo = f("%s - [Tracking ID = %s]", getCurrentMethodName(), trackingId);

    retryIfAssertionErrorOrRuntimeExceptionOccurred(() ->
    {
      Long routeId = get(KEY_CREATED_ROUTE_ID);
      VanInboundRequest vanInboundRequest = new VanInboundRequest();
      vanInboundRequest.setTrackingId(trackingId);
      vanInboundRequest.setWaypointId(deliveryWaypointId);
      vanInboundRequest.setType("VAN_FROM_NINJAVAN");
      getInboundClient().vanInbound(vanInboundRequest, routeId);
    }, methodInfo);
  }

}
