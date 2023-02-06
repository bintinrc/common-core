package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.core.model.shipper.Industry;
import co.nvqa.common.core.model.shipper.SalesPerson;
import io.restassured.response.Response;
import java.util.TimeZone;

public class MiscCoreClient extends SimpleApiClient {

  public MiscCoreClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public MiscCoreClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_CAMEL_CASE_MAPPER);
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

}
