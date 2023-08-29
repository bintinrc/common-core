package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.inject.Singleton;

@Singleton
public class ParameterClient extends SimpleApiClient {

  public ParameterClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_SNAKE_CASE_MAPPER);
  }

  public Response setParameters(String body) {
    String url = "core/parameter";
    RequestSpecification spec = createAuthenticatedRequest()
        .body(body);

    return doPut("Set Paramenter", spec, url);
  }

}