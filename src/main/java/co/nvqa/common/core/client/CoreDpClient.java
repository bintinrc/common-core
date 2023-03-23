package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.dp.CustomerCollectRequest;
import co.nvqa.common.core.model.dp.DpTagging;
import co.nvqa.common.core.model.dp.DpUntagging;
import co.nvqa.common.core.model.dp.LodgeInRequest;
import co.nvqa.common.core.model.event.Events;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
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

    Response r = doPost("CORE - DRIVER DROP OFF TO DP", spec, uri);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    return r;
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

  public DpTagging tagToDpAndAddToRoute(long orderId, DpTagging request) {
    String uri = "core/2.0/orders/{orderId}/dps/routes-dp";
    String json = toJsonSnakeCase(request);
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);

    Response r = doPut("Core - Tag to DP and Add to Route", requestSpecification, uri);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), DpTagging.class);
  }

  public void removeFromDpHoldingRouteAndUntagFromDp(long orderId, DpUntagging request) {
    String json = toJsonSnakeCase(request);
    String url = "core/2.0/orders/{orderId}/dps/routes-dp";
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);
    Response response = doDelete(
        "API Core - Remove From To DP Holding Route And Untag From DP", requestSpecification,
        url);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }
}
