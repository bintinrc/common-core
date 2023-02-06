package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.SmsNotificationsSettings;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.inject.Singleton;

@Singleton
public class CoreNotificationsClient extends SimpleApiClient {

  public CoreNotificationsClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
  }

  public SmsNotificationsSettings getSmsNotificationsSettings() {
    String apiMethod = "core/notifications/sms/settings";

    RequestSpecification requestSpecification = createAuthenticatedRequest();

    Response r = doGet("Notifications Client - Get SMS notifications settings",
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJsonSnakeCase(r.getBody().asString(), SmsNotificationsSettings.class);
  }

  public SmsNotificationsSettings updateSmsNotificationsSettings(
      SmsNotificationsSettings settings) {
    String apiMethod = "core/notifications/sms/settings";
    String json = toJsonSnakeCase(settings);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(json);

    Response r = doPut("Notifications Client - update SMS notifications settings",
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJsonSnakeCase(r.getBody().asString(), SmsNotificationsSettings.class);
  }

}
