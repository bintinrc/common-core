package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.CoreDpClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.dp.DpTagging;
import co.nvqa.common.core.model.route.AddParcelToRouteRequest;
import io.cucumber.java.en.And;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.assertj.core.api.Assertions;

public class ApiDpSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private CoreDpClient coreDpClient;

  @Override
  public void init() {

  }

  /**
   * API Core - Operator perform dp drop off with order id "{string}"
   *
   * @param orderIdString example: {KEY_LIST_OF_CREATED_ORDERS[1].id}
   */
  @And("API Core - Operator perform dp drop off with order id {string}")
  public void operatorPerformDpDropOff(String orderIdString) {
    Long orderId = Long.parseLong(resolveValue(orderIdString));

    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getCoreDpClient().driverDropOffToDp(orderId), "core drop off dp");
  }

  @And("API Core - Operator tag to dp for the order:")
  public void operatorTagToDp(Map<String, String> source) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(source);
    final String jsonRequest = resolvedDataTable.get("request");
    final long orderId = Long.parseLong(resolvedDataTable.get("orderId"));
    final String trackingId = resolvedDataTable.get("trackingId");
    final DpTagging request = fromJsonSnakeCase(
        jsonRequest, DpTagging.class);
    retryIfAssertionErrorOccurred(
        () -> {
          DpTagging result = getCoreDpClient().tagToDpAndAddToRoute(orderId, request);
          Assertions.assertThat(result.getDpId()).as("dp_id")
              .isEqualTo(request.getDpTag().getDpId());
          Assertions.assertThat(result.getOrderId()).as("order_id")
              .isEqualTo(orderId);
          Assertions.assertThat(result.getStatus()).as("status")
              .isEqualTo("SUCCESS");
          Assertions.assertThat(result.getMessage()).as("message")
              .isEqualTo(
                  f("Added %s to route %s", trackingId, request.getAddToRoute().getRouteId()));
          Assertions.assertThat(result.getTrackingId()).as("tracking_id")
              .isEqualTo(trackingId);
        },
        "tag to dp");
  }
}
