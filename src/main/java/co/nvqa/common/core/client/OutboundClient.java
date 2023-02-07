package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.OutboundRequest;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import javax.inject.Singleton;

@Singleton
public class OutboundClient extends SimpleApiClient {

  public OutboundClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public void outboundScan(OutboundRequest request) {
    OutboundRequest[] requests = new OutboundRequest[]{request};
    outboundScan(requests);
  }

  public void outboundScan(OutboundRequest[] requests) {
    String uri = "core/scans/outbounds";
    String json = toJson(requests);

    doPost("Scans - Outbound Scan", createAuthenticatedRequest().body(json), uri);
  }

  public void outboundScanObsolete(OutboundRequest request) {
    OutboundRequest[] requests = new OutboundRequest[]{request};
    outboundScanObsolete(requests);
  }

  public void outboundScanObsolete(OutboundRequest[] requests) {
    Response r = outboundAndGetRawResponseObsolete(requests);
    if (r.statusCode() != HttpConstants.RESPONSE_201_CREATED) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.TEXT);
  }

  public Response outboundAndGetRawResponseObsolete(OutboundRequest[] requests) {
    String uri = "core/outbound";
    String json = toJson(requests);

    return doPost("CORE - OUTBOUND SCAN", createAuthenticatedRequest().body(json), uri);
  }

  public void confirmScans(OutboundRequest request) {
    OutboundRequest[] requests = new OutboundRequest[]{request};
    confirmScans(requests);
  }

  public void confirmScans(OutboundRequest[] requests) {
    Response r = confirmScansAndGetRawResponse(requests);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public Response confirmScansAndGetRawResponse(OutboundRequest[] requests) {
    String url = "core/outbound/confirmScans";
    String json = toJson(requests);

    return doPost("CORE - OUTBOUND SCAN", createAuthenticatedRequest().body(json), url);
  }
}