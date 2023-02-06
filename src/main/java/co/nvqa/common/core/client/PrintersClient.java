package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.PrinterSettings;
import co.nvqa.common.utils.NvTestHttpException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.TimeZone;

public class PrintersClient extends SimpleApiClient {

  public PrintersClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public PrintersClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_CAMEL_CASE_MAPPER);
  }

  public PrinterSettings addPrinter(PrinterSettings printerSettings) {
    String apiMethod = "core/printers/create";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(toJsonSnakeCase(printerSettings));

    Response r = doPost("Printers Client - Create Printer", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJsonSnakeCase(r.getBody().asString(), PrinterSettings.class);
  }

  public void delete(long printerId) {
    String apiMethod = "core/printers/{printerId}";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("printerId", printerId);

    Response r = doDelete("Printers Client - Delete printer", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public List<PrinterSettings> getAll() {
    String apiMethod = "core/printers/get-all";

    RequestSpecification requestSpecification = createAuthenticatedRequest();

    Response r = doGet("Printers Client - Get All Printers", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJsonSnakeCaseToList(r.getBody().asString(), PrinterSettings.class);
  }
}
