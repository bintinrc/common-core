package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.SmsNotificationsSettings;
import co.nvqa.common.utils.NvTestHttpException;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.TimeZone;

public class CoreNotificationsClient extends SimpleApiClient {

  public CoreNotificationsClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public CoreNotificationsClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_SNAKE_CASE_MAPPER);
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
