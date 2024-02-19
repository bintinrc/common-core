package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

@Singleton
public class DebugClient extends SimpleApiClient {

  public DebugClient() {
    super(TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public Map<String, Object> getTotalCod(Long driverId, String routeDate) {
    String url = "core/debug/driver/{driverId}/total-cod";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("driverId", driverId)
        .queryParam("routeDate", routeDate)
        .queryParam("refresh", false);

    Response r = doGet("GET TOTAL COD", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return r.getBody().as(HashMap.class);
  }

}
