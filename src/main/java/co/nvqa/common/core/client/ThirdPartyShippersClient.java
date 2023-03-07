package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.ThirdPartyShippers;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import javax.inject.Singleton;

@SuppressWarnings("WeakerAccess")
@Singleton
public class ThirdPartyShippersClient extends SimpleApiClient {

  public ThirdPartyShippersClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public ThirdPartyShippers create(ThirdPartyShippers thirdPartyShippers) {
    String apiMethod = "core/thirdpartyshippers";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(toJson(thirdPartyShippers));

    Response r = doPost("Third Party Shippers Client - Create Third Party Shippers",
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), ThirdPartyShippers.class);
  }

  public ThirdPartyShippers update(ThirdPartyShippers thirdPartyShippers) {
    String apiMethod = "core/thirdpartyshippers/{tpsId}";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("tpsId", thirdPartyShippers.getId())
        .body(toJson(thirdPartyShippers));

    Response r = doPut("Third Party Shippers Client - Update Third Party Shippers details",
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), ThirdPartyShippers.class);
  }

  public List<ThirdPartyShippers> getAll() {
    String apiMethod = "core/thirdpartyshippers";
    RequestSpecification requestSpecification = createAuthenticatedRequest();

    Response r = doGet("Third Party Shippers Client - Third Party Shippers List",
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJsonToList(r.body().asString(), ThirdPartyShippers.class);
  }

  public ThirdPartyShippers delete(int shipperId) {
    String apiMethod = "core/thirdpartyshippers/{shipperId}";
    RequestSpecification requestSpecification = createAuthenticatedRequest().
        pathParam("shipperId", shipperId);

    Response r = doDelete("Third Party Shippers Client - Delete Third Party Shipper",
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), ThirdPartyShippers.class);
  }
}
