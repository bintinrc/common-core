package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.CoreDpClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.dp.DpTagging;
import co.nvqa.common.core.model.dp.DpUntagging;
import io.cucumber.java.en.And;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.assertj.core.api.Assertions;

public class ApiDpSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private CoreDpClient coreDpClient;

  @And("API Core - Operator tag to dp for the order:")
  public void operatorTagToDp(Map<String, String> source) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(source);
    final String jsonRequest = resolvedDataTable.get("request");
    final long orderId = Long.parseLong(resolvedDataTable.get("orderId"));
    final String trackingId = resolvedDataTable.get("trackingId");
    final DpTagging request = fromJsonSnakeCase(
        jsonRequest, DpTagging.class);
    doWithRetry(
        () -> {
          DpTagging result = getCoreDpClient().tagToDpAndAddToRoute(orderId, request);
          if (request.getDpTag() != null) {
            Assertions.assertThat(result.getDpId()).as("dp_id")
                .isEqualTo(request.getDpTag().getDpId());
          } else {
            Assertions.assertThat(result.getDpId()).as("dp_id")
                .isNull();
          }
          if (request.getAddToRoute() != null) {
            Assertions.assertThat(result.getStatus()).as("status")
                .isEqualTo("SUCCESS");
            Assertions.assertThat(result.getMessage()).as("message")
                .isEqualTo(
                    f("Added %s to route %s", trackingId, request.getAddToRoute().getRouteId()));

          } else {
            Assertions.assertThat(result.getStatus()).as("status")
                .isNull();
            Assertions.assertThat(result.getMessage()).as("message")
                .isNull();
          }
          Assertions.assertThat(result.getOrderId()).as("order_id")
              .isEqualTo(orderId);
          Assertions.assertThat(result.getTrackingId()).as("tracking_id")
              .isEqualTo(trackingId);
        },
        "tag to dp");
  }

  @And("API Core - Operator untag from dp and remove from holding route:")
  public void operatorUnTagFromDpAndRemoveFromRoute(Map<String, String> source) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(source);
    final String jsonRequest = resolvedDataTable.get("request");
    final long orderId = Long.parseLong(resolvedDataTable.get("orderId"));
    final DpUntagging request = fromJsonSnakeCase(
        jsonRequest, DpUntagging.class);
    doWithRetry(
        () -> getCoreDpClient().removeFromDpHoldingRouteAndUntagFromDp(orderId, request),
        "untag from dp");
  }

  @And("API Core - Operator untag from dp:")
  public void operatorUntagFromDp(Map<String, String> source) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(source);
    final long orderId = Long.parseLong(resolvedDataTable.get("orderId"));
    final String version = resolvedDataTable.get("version");
    doWithRetry(
        () -> getCoreDpClient().untagOrderFromDp(orderId, version),
        "untag from dp");
  }

  @And("API Core - Operator overstay order from dp:")
  public void operatorOverstayFromDp(Map<String, String> source) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(source);
    final long orderId = Long.parseLong(resolvedDataTable.get("orderId"));
    final long dpId = Long.parseLong(resolvedDataTable.get("dpId"));
    doWithRetry(
        () -> getCoreDpClient().overstayFromDp(orderId, dpId),
        "overstay from dp");
  }
}
