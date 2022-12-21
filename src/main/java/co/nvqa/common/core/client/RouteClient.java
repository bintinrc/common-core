package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.route.AddParcelToRouteRequest;
import co.nvqa.common.core.model.route.AddPickupJobToRouteRequest;
import co.nvqa.common.core.model.route.RouteRequest;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.core.model.waypoint.Waypoint;
import co.nvqa.common.utils.NvTestHttpException;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.TimeZone;

public class RouteClient extends SimpleApiClient {

  public RouteClient(String baseUrl, String bearerToken) {
    this(baseUrl, bearerToken, null);
  }

  public RouteClient(String baseUrl, String bearerToken, TimeZone timeZone) {
    super(baseUrl, bearerToken, timeZone, DEFAULT_CAMEL_CASE_MAPPER);
  }

  public RouteResponse createRoute(RouteRequest routeRequest) {
    Response r = createRouteAndGetRawResponse(routeRequest);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);

    RouteResponse[] routeResponses = fromJson(r.body().asString(), RouteResponse[].class);
    return routeResponses[0];
  }

  public Response createRouteAndGetRawResponse(RouteRequest routeRequest) {
    RouteRequest[] createRouteRequests = new RouteRequest[]{routeRequest};

    String url = "core/routes";
    String json = toJson(createRouteRequests);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPost("Operator Portal - Create Route", spec, url);
  }

  public void addParcelToRoute(long orderId, AddParcelToRouteRequest addParcelToRouteRequest) {
    final Response response = addParcelToRouteAndGetRawResponse(orderId, addParcelToRouteRequest);
    response.then().contentType(ContentType.JSON);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }

  public Response addParcelToRouteAndGetRawResponse(long orderId,
      AddParcelToRouteRequest addParcelToRouteRequest) {
    final String url = "core/2.0/orders/{orderId}/routes";
    final String json = toJson(DEFAULT_SNAKE_CASE_MAPPER, addParcelToRouteRequest);

    final RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(json);

    return doPut("Operator Portal - Add Parcel to Route", spec, url);
  }

  public Response archiveRouteV2AndGetRawResponse(long routeId) {
    final String url = "route-v2/routes/{routeId}/archive-status?state=true";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId);

    return doPut("Operator API - Archive Route V2", requestSpecification, url);
  }

  public void archiveRouteV2(long routeId) {
    Response r = archiveRouteV2AndGetRawResponse(routeId);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void addPickupJobToRoute(long jobId,
      AddPickupJobToRouteRequest addPickupJobToRouteRequest) {
    final Response response = addPickupJobToRouteAndGetRawResponse(jobId,
        addPickupJobToRouteRequest);
    response.then().contentType(ContentType.JSON);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }

  private Response addPickupJobToRouteAndGetRawResponse(long jobId,
      AddPickupJobToRouteRequest addPickupJobToRouteRequest) {
    final String url = "core/pickup-appointment-jobs/{jobId}/route";
    final String json = toJson(DEFAULT_SNAKE_CASE_MAPPER, addPickupJobToRouteRequest);

    final RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("jobId", jobId)
        .body(json);

    return doPut("Operator Portal - Add Pickup Job to Route", spec, url);
  }

  public Response updateWaypointToPendingAndGetRawResponse(
      List<Waypoint> request) {
    final String url = "core/waypoints";
    final String json = toJson(request);

    final RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPut("Operator Portal - Update Routed Waypoint to Pending", spec, url);
  }

  public List<Waypoint> updateWaypointToPending(List<Waypoint> request) {
    final Response response = updateWaypointToPendingAndGetRawResponse(request);
    response.then().contentType(ContentType.JSON);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
    return fromJsonSnakeCaseToList(response.getBody().asString(), Waypoint.class);
  }
}
