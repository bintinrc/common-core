package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.CoreNotificationsClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import io.cucumber.java.en.When;
import javax.inject.Inject;
import lombok.Getter;

public class ApiMessagingSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private CoreNotificationsClient notificationsClient;

  /*
   * API step to get SMS notification for shippers
   */
  @When("API Core - Operator gets SMS notifications settings")
  public void apiOperatorGetsSMSNotificationsSettings() {
    doWithRetry(() ->
            put(KEY_CORE_SMS_NOTIFICATIONS_SETTINGS,
                getNotificationsClient().getSmsNotificationsSettings()),
        "get sms notification settings");
  }
}
