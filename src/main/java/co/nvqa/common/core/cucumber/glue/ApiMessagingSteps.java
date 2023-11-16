package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.MessagingClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.MessagingHistoryResponse;
import co.nvqa.common.core.model.messaging.SendSmsRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;

public class ApiMessagingSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private MessagingClient messagingClient;


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

  /**
   * Send sms with following datatable
   *<pre>
   * Given API Core - Operator send sms with following data
   *       | template      | test {{tracking_id}}             |
   *       | trackingId    | {KEY_CREATED_ORDER_TRACKING_ID}  |
   * </pre>
   */
  @When("API Core - Operator send sms with following data")
  public void apiOperatorSendSms(Map<String, Object> data) {
    var resolvedData = resolveKeyValues(data);
    var req = new SendSmsRequest();
    req.setTemplate((String) resolvedData.get("template"));
    var smsPayload = Map.of(
        "tracking_id", resolvedData.get("trackingId"),
        "name", resolvedData.getOrDefault("name", "qa"),
        "email", resolvedData.getOrDefault("email", "qa@ninjavan.co"),
        "job", resolvedData.getOrDefault("job", "QA")
    );
    req.setOrderDetails(List.of(smsPayload));
    doWithRetry(() -> messagingClient.sendSms(req), "Send SMS");
  }
}
