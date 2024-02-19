package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.driver.DriverType;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class RbeDriverTypeClient extends SimpleApiClient {

  public RbeDriverTypeClient() {
    super(TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public DriverType validate(DriverType driverType) {
    String apiMethod = "route/rbe/rules/drivertype";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(toJson(driverType));

    Response r = doPost("Driver Type Client - Validate Driver Type", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), DriverType.class);
  }

  public List<DriverType> getRbeAllDriverType() {
    String apiMethod = "route/rbe/rules/drivertype";

    RequestSpecification requestSpecification = createAuthenticatedRequest();

    Response r = doGet("Driver Type Client - Get All Driver Type", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJsonToList(r.body().asString(), DriverType.class);
  }

  public DriverType updateDriverTypeCondition(DriverType driverType) {
    String apiMethod = "route/rbe/rules/drivertype/{driverTypeId}";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("driverTypeId", driverType.getId())
        .body(toJson(driverType));

    Response r = doPut("Driver Type Client - Update Driver Type Conditions", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), DriverType.class);
  }

}
