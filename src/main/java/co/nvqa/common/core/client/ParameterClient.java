package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.core.utils.CoreTestUtils;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

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
    String uniqueString = CoreTestUtils.generateUniqueId();
    if (StringUtils.endsWithIgnoreCase(routeGroup.getName(), "{uniqueString}")) {
      routeGroup.setName(routeGroup.getName().replace("{uniqueString}", uniqueString));
    }
  }

}