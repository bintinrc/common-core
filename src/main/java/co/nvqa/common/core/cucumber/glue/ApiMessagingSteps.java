package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.MessagingClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.MessagingHistoryResponse;
import io.cucumber.java.en.Then;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;

public class ApiMessagingSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private MessagingClient messagingClient;

  @Override
  public void init() {

  }

  /**
   * <br/> <b>trackingId</b>: order ID of the order/parcel<br/>
   */
  @Then("API Core - Operator gets all the SMS notification by Tracking ID {string}")
  public void apiOperatorGetsAllTheSmsNotificationByTrackingId(String trackingId) {
    String tid = resolveValue(trackingId);
    doWithRetry(
        () -> {
          List<MessagingHistoryResponse> messagingHistoryResponses =
              getMessagingClient().getMessageNotificationBasedOnTrackingId(tid);

          for (MessagingHistoryResponse historyResponse : messagingHistoryResponses) {
            putInList(KEY_CORE_LIST_OF_MESSAGING_HISTORY_DATA, historyResponse);
          }
        }, "get sms notifs");
  }
}
