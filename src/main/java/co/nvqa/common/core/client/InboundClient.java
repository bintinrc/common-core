package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.VanInboundRequest;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class InboundClient extends SimpleApiClient {
  public InboundClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public Response vanInboundAndGetRawResponse(VanInboundRequest[] requests, long routeId) {
    String uri = "core/routes/{routeId}/van-inbounds";
    String json = toJson(requests);

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .body(json);

    return doPost("CORE - VAN INBOUND", spec, uri);
  }
  public void vanInbound(VanInboundRequest[] requests, long routeId) {
    Response r = vanInboundAndGetRawResponse(requests, routeId);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }
  public Response vanInboundAndGetRawResponse(VanInboundRequest request, long routeId) {
    VanInboundRequest[] requests = new VanInboundRequest[]{request};
    return vanInboundAndGetRawResponse(requests, routeId);
  }
  public void vanInbound(VanInboundRequest request, long routeId) {
    VanInboundRequest[] requests = new VanInboundRequest[]{request};
    vanInbound(requests, routeId);
  }
}
