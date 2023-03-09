package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.OrderJaroScoresV2Info;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;

public class OrderJaroScoresV2Client extends SimpleApiClient {

  public OrderJaroScoresV2Client() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
  }

  public List<OrderJaroScoresV2Info> fetchRouteGroupAddresses(Long routeGroupId) {
    String url = "core/orderjaroscoresv2/routegroup/{routeGroupId}";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeGroupId", routeGroupId);

    Response r = doGet("Core - Fetch Route group Addresses", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return JsonUtils.fromJsonToList(r.body().asString(), OrderJaroScoresV2Info.class);
  }

  public List<OrderJaroScoresV2Info> fetchZoneAddresses(Long zoneId, int size) {
    String url = "core/orderjaroscoresv2/fetch";

    RequestSpecification spec = createAuthenticatedRequest()
        .queryParam("zone_id", zoneId)
        .queryParam("size", size);

    Response r = doGet("Core - Fetch Zone Addresses", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return JsonUtils.fromJsonToList(r.body().asString(), OrderJaroScoresV2Info.class);
  }

}