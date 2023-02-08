package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.RouteMonitoringResponse;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class RouteMonitoringClient extends SimpleApiClient {

  public RouteMonitoringClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }


  public List<RouteMonitoringResponse> getRouteMonitoringDetails(String date, List<Long> hubIds,
      List<Long> zoneIds, int pageSize) {
    List<RouteMonitoringResponse> temp = new ArrayList<>();
    long routeId = -1;
    Response r = getRouteMonitoringAsRawResponse(date, hubIds, zoneIds, pageSize, routeId);
    List<RouteMonitoringResponse> responses = fromJsonSnakeCaseToList(r.body().asString(),
        RouteMonitoringResponse.class);
    temp.addAll(responses);
    while (responses.size() >= pageSize) {
      routeId = responses.get(responses.size() - 1).getRouteId();
      Response resp = getRouteMonitoringAsRawResponse(date, hubIds, zoneIds, pageSize, routeId);
      responses = fromJsonSnakeCaseToList(resp.body().asString(), RouteMonitoringResponse.class);
      temp.addAll(responses);
    }
    return temp;
  }

  public Response getRouteMonitoringAsRawResponse(String date, List<Long> hubIds,
      List<Long> zoneIds, int pageSize, long routeId) {
    String url = "/core/3.0/routes/monitoring";
    String hubIdsString = Arrays.toString(hubIds.toArray());
    String zoneIdsString = Arrays.toString(zoneIds.toArray());
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .queryParam("date", date)
        .queryParam("hub_ids_string", hubIdsString.substring(1, hubIdsString.length() - 1))
        .queryParam("zone_ids_string", zoneIdsString.substring(1, zoneIdsString.length() - 1))
        .queryParam("page_size", pageSize)
        .queryParam("prev_last_route_id", routeId);
    Response r = doGet(
        f("Core - Get Route Monitoring Details for date %s, hub ids %s, zone ids %s", date, hubIds,
            zoneIds), requestSpecification, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return r;
  }

  public RouteMonitoringResponse getParcelDetails(long routeId, String category, String jobType) {
    Response r = getParcelDetailsRawResponse(routeId, category, jobType.toLowerCase());
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJsonSnakeCase(r.body().asString(), RouteMonitoringResponse.class);
  }

  public Response getParcelDetailsRawResponse(long routeId, String category, String jobType) {
    String url = "core/3.0/routes/monitoring/routes/{routeId}";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .queryParam("category", category)
        .queryParam("jobType", jobType);

    return doGet("Core - Route Monitoring - Get Route Monitoring Parcel Details",
        requestSpecification, url);
  }

  public RouteMonitoringResponse getPendingPriorityParcelDetails(long routeId, String jobType) {
    return getParcelDetails(routeId, "pending_priority", jobType);
  }

  public RouteMonitoringResponse getInvalidFailedDeliveries(long routeId) {
    return getParcelDetails(routeId, "invalid_failed", "dd");
  }

  public RouteMonitoringResponse getInvalidFailedPickups(long routeId) {
    return getParcelDetails(routeId, "invalid_failed", "pp");
  }

  public RouteMonitoringResponse getInvalidFailedReservations(long routeId) {
    return getParcelDetails(routeId, "invalid_failed", "pickup_appointment");
  }
}