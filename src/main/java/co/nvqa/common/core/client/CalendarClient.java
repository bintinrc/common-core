package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.BlockedDate;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class CalendarClient extends SimpleApiClient {

  public CalendarClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public BlockedDate[] getBlockedDates() {
    String url = "core/calendar/blockeddates";

    Response r = doGet("GET BLOCKED DATES", createAuthenticatedRequest(), url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    return fromJson(r.body().asString(), BlockedDate[].class);
  }

  public int addBlockedDates(List<BlockedDate> dates) {
    String url = "core/calendar/addblockeddates/";
    String json = toJson(dates);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    Response r = doPost("ADD BLOCKED DATES", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }

    return Integer.parseInt(r.body().asString());
  }

  public int deleteBlockedDates(List<BlockedDate> dates) {
    String url = "core/calendar/deleteblockeddates/";
    String json = toJson(dates);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    Response r = doPost("REMOVE BLOCKED DATES", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return Integer.parseInt(r.body().asString());
  }

}
