package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.Lazada3PL;
import co.nvqa.common.utils.NvTestHttpException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.TimeZone;

public class Lazada3PLClient extends SimpleApiClient {

  public Lazada3PLClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public Lazada3PLClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_SNAKE_CASE_MAPPER);
  }

  public void postLazada3PL(Lazada3PL lazada3PLRequests) {
    Response r = postLazada3PLAndGetRawResponse(lazada3PLRequests);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public Response postLazada3PLAndGetRawResponse(Lazada3PL lazada3PL) {
    Lazada3PL[] lazada3PLRequests = new Lazada3PL[]{lazada3PL};

    String url = "core/tools/upload-rejected-parcel-list";
    String json = toJson(lazada3PLRequests);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPost("Operator Portal - Post Lazada 3PL", spec, url);
  }

}
