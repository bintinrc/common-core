package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.Lazada3PL;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.inject.Singleton;

@Singleton
public class Lazada3PLClient extends SimpleApiClient {

  public Lazada3PLClient() {
    super(TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
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

    return doPost("Core - Post Lazada 3PL", spec, url);
  }

}
