package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.core.model.CodInbound;
import co.nvqa.common.utils.JsonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.TimeZone;

public class CodInboundsClient extends SimpleApiClient {

  public CodInboundsClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public CodInboundsClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_CAMEL_CASE_MAPPER);
  }

  public Response codInbound(CodInbound request) {
    String uri = "core/codinbounds";
    String json = JsonUtils.toJson(JsonUtils.getDefaultSnakeCaseMapper(), request);
    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPost("CORE - COD INBOUND", spec, uri);
  }

}
