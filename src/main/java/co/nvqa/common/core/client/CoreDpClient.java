package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.core.model.dp.CustomerCollectRequest;
import co.nvqa.common.core.model.dp.LodgeInRequest;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.inject.Singleton;

@Singleton
public class CoreDpClient extends SimpleApiClient {

  public CoreDpClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
  }

  public Response lodgeInToDp(long orderId, LodgeInRequest request) {
    String uri = "core/2.0/orders/{orderId}/lodgein";
    String json = toJson(DEFAULT_SNAKE_CASE_MAPPER, request);
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);

    return doPost("CORE - LODGE IN TO DP", spec, uri);
  }

  public Response driverDropOffToDp(long orderId) {
    String uri = "core/2.0/orders/{orderId}/dropoff";
    String json = "{}";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);

    return doPost("CORE - DRIVER DROP OFF TO DP", spec, uri);
  }

  public Response customerCollect(long orderId, CustomerCollectRequest request) {
    String uri = "core/2.0/orders/{orderId}/collect";
    String json = toJson(DEFAULT_SNAKE_CASE_MAPPER, request);
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);

    return doPost("CORE - CUSTOMER COLLECT FROM DP", spec, uri);
  }

  public Response untagOrderFromDp(long orderId, long userId) {
    String uri = "core/2.0/orders/{orderId}/dps";
    String json = "{\"user_id\":" + userId + "}";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);

    return doDelete("CORE - UNTAG ORDER FROM DP", spec, uri);
  }

}
