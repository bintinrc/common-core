package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.miscellanous.Industry;
import co.nvqa.common.core.model.miscellanous.SalesPerson;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.inject.Singleton;

@Singleton
public class MiscCoreClient extends SimpleApiClient {

  public MiscCoreClient(String baseUrl, String token) {
    super(baseUrl, token,
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public MiscCoreClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public Industry[] readIndustries() {
    String url = "core/industries";

    Response response = doGet("CORE - READ INDUSTRIES", createAuthenticatedRequest(), url);
    return fromJson(response.body().asString(), Industry[].class);
  }

  public SalesPerson[] readSalesPerson() {
    String url = "core/1.0/sales";

    Response response = doGet("CORE - READ SALES PERSONS", createAuthenticatedRequest(), url);
    return fromJson(response.body().asString(), SalesPerson[].class);
  }

  public SalesPerson createSalesPerson(SalesPerson salesPerson) {
    String apiMethod = "/core/1.0/sales";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(salesPerson);

    Response r = doPost("Sales Client - Create Sales Person", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return r.getBody().as(SalesPerson.class);
  }

}
