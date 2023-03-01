package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.route.AddParcelToRouteRequest;
import co.nvqa.common.core.model.route.AddPickupJobToRouteRequest;
import co.nvqa.common.core.model.route.RouteRequest;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.core.model.waypoint.Waypoint;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class RouteClient extends SimpleApiClient {

  public RouteClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
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

  /**
   * @param orderId orderId
   * @param transactionType String between DELIVERY and PICKUP
   */
  public void pullFromRoute(long orderId, String transactionType) {
    final String url = "core/2.0/orders/{orderId}/routes";
    final RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"type\":\"%s\"}", transactionType));

    doPut("Operator Portal - Pull Parcel from Route", spec, url);
  }

  public void archiveRoute(long routeId) {
    Response r = archiveRouteAndGetRawResponse(routeId);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void archiveRoutes(List<Long> routeIds) {
    for (long routeId : routeIds) {
      archiveRoute(routeId);
    }
  }

  public Response archiveRouteAndGetRawResponse(long routeId) {
    final String url = "route-v2/routes/{routeId}/archive";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId);

    return doPut("Operator API - Archive Route", requestSpecification, url);
  }

  public void unarchiveRoutes(List<Long> routeIds) {
    for (long routeId : routeIds) {
      unarchiveRoute(routeId);
    }
  }

  public void unarchiveRoute(long routeId) {
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId);

    Response r = unarchiveRouteAndGetRawResponse(routeId);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public Response unarchiveRouteAndGetRawResponse(long routeId) {
    final String url = "route-v2/routes/{routeId}/unarchive";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId);

    return doPut("Operator API - Unarchive Route", requestSpecification, url);
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

  public void addReservationToRoute(long routeId, long reservationId) {
    String url = "core/2.0/reservations/{reservation_id}/route";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservation_id", reservationId)
        .body(f("{\"new_route_id\":%d,\"route_index\":-1,\"overwrite\":true}", routeId));

    Response r = doPut("Reservation V2 - Add Reservation to Route", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void pullReservationOutOfRoute(long reservationId) {
    String url = "core/2.0/reservations/{reservation_id}/unroute";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservation_id", reservationId)
        .body("{}");

    Response r = doPut("Reservation V2 - Pull Reservation Out of Route", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().assertThat().body(equalTo(f("{\"id\":%d,\"status\":\"PENDING\"}", reservationId)));
  }
}
