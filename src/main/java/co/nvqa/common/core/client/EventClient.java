package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.event.Events;
import co.nvqa.common.utils.NvTestHttpException;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.TimeZone;

public class EventClient extends SimpleApiClient {

  public EventClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public EventClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_SNAKE_CASE_MAPPER);
  }

  public Events getOrderEventsByOrderId(Long orderId) {
    String apiMethod = f("events/1.0/orders/{orderId}/events", orderId);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("orderId", orderId);

    Response r = doGet("Event Client - Get order events", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), Events.class);
  }
}
