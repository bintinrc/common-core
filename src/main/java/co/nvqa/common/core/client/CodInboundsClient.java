package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.core.model.CodInbound;
import co.nvqa.common.utils.JsonUtils;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.inject.Singleton;

@Singleton
public class CodInboundsClient extends SimpleApiClient {

  public CodInboundsClient() {
    super(TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public Response codInbound(CodInbound request) {
    String uri = "core/codinbounds";
    String json = JsonUtils.toJson(JsonUtils.getDefaultSnakeCaseMapper(), request);
    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPost("CORE - COD INBOUND", spec, uri);
  }

}
