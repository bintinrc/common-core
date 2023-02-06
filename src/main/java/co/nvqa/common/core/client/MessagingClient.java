package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.MessagingHistoryResponse;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class MessagingClient extends SimpleApiClient {

  public MessagingClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public List<MessagingHistoryResponse> getMessageNotificationBasedOnTrackingId(String trackingId) {
    String apiMethod = f("core/messaging/orders/sms/history?tracking_id={trackingId}", trackingId);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("trackingId", trackingId)
        .header("Accept", "application/json, text/plain, */*");

    Response r = doGet(
        f("Messaging Client - Get SMS Notifications with Tracking ID : %s", trackingId),
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);

    return fromJsonToList(r.body().asString(), MessagingHistoryResponse.class);
  }

}
