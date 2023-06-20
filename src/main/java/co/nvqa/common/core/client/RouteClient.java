package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.RouteGroup;
import co.nvqa.common.core.model.coverage.CreateCoverageRequest;
import co.nvqa.common.core.model.coverage.CreateCoverageResponse;
import co.nvqa.common.core.model.pickup.MilkRunGroup;
import co.nvqa.common.core.model.reservation.BulkRouteReservationResponse;
import co.nvqa.common.core.model.route.AddParcelToRouteRequest;
import co.nvqa.common.core.model.route.AddPickupJobToRouteRequest;
import co.nvqa.common.core.model.route.BulkAddPickupJobToRouteRequest;
import co.nvqa.common.core.model.route.BulkAddPickupJobToRouteResponse;
import co.nvqa.common.core.model.route.GetRouteDetailsResponse;
import co.nvqa.common.core.model.route.MergeWaypointsResponse;
import co.nvqa.common.core.model.route.ParcelRouteTransferRequest;
import co.nvqa.common.core.model.route.ParcelRouteTransferResponse;
import co.nvqa.common.core.model.route.RouteRequest;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.core.model.waypoint.Waypoint;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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

    return doPost("Core - Create Route", spec, url);
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

    return doPut("Core - Add Parcel to Route", spec, url);
  }

  /**
   * @param orderId         orderId
   * @param transactionType String between DELIVERY and PICKUP
   */
  public void pullFromRoute(long orderId, String transactionType) {
    final String url = "core/2.0/orders/{orderId}/routes";
    final RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"type\":\"%s\"}", transactionType));

    Response response = doDelete("Core - Pull Out Order From Route",
        spec, url);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
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

    return doPut("Route - Archive Route", requestSpecification, url);
  }

  public void unarchiveRoutes(List<Long> routeIds) {
    for (long routeId : routeIds) {
      unarchiveRoute(routeId);
    }
  }

  public void unarchiveRoute(long routeId) {
    Response r = unarchiveRouteAndGetRawResponse(routeId);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public Response unarchiveRouteAndGetRawResponse(long routeId) {
    final String url = "route-v2/routes/{routeId}/unarchive";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId);

    return doPut("API Route - Unarchive Route", requestSpecification, url);
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

    return doPut("Core - Add Pickup Job to Route", spec, url);
  }

  public BulkAddPickupJobToRouteResponse bulkAddPickupJobToRoute(
      BulkAddPickupJobToRouteRequest bulkAddPickupJobToRouteRequest) {
    final String url = "core/pickup-appointment-jobs/route-bulk";
    final String json = toJson(DEFAULT_SNAKE_CASE_MAPPER, bulkAddPickupJobToRouteRequest);
    final RequestSpecification spec = createAuthenticatedRequest().body(json);
    final Response response = doPut("Core - Bulk Add Pickup Job to Route", spec, url);
    response.then().contentType(ContentType.JSON);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
    return fromJsonSnakeCase(response.getBody().asString(), BulkAddPickupJobToRouteResponse.class);
  }

  public void removePAJobFromRoute(long paJobId) {
    String uri = "core/pickup-appointment-jobs/{paJobId}/unroute";
    RequestSpecification spec = createAuthenticatedRequest().pathParam("paJobId", paJobId);
    Response r = doPut("Core - Remove PA Job from Route", spec, uri);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public Response updateWaypointToPendingAndGetRawResponse(
      List<Waypoint> request) {
    final String url = "core/waypoints";
    final String json = toJson(request);

    final RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPut("Core - Update Routed Waypoint to Pending", spec, url);
  }

  public List<Waypoint> updateWaypointToPending(List<Waypoint> request) {
    final Response response = updateWaypointToPendingAndGetRawResponse(request);
    response.then().contentType(ContentType.JSON);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
    return fromJsonSnakeCaseToList(response.getBody().asString(), Waypoint.class);
  }

  public Response addReservationToRouteAndGetRawResponse(long routeId, long reservationId,
      Boolean overwrite) {
    final String url = "core/2.0/reservations/{reservationId}/route";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservationId", reservationId)
        .body(f("{\"new_route_id\":%d,\"route_index\":-1,\"overwrite\":%s}", routeId,
            String.valueOf(overwrite)));
    return doPut("Core - Add Reservation to Route", spec, url);
  }

  public void addReservationToRoute(long routeId, long reservationId, Boolean overwrite) {
    Response r = addReservationToRouteAndGetRawResponse(routeId, reservationId, overwrite);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public BulkRouteReservationResponse bulkAddToRouteReservation(String request) {
    Response r = bulkAddToRouteReservationAndGetRawResponse(request);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJsonSnakeCase(r.getBody().asString(), BulkRouteReservationResponse.class);
  }

  public Response bulkAddToRouteReservationAndGetRawResponse(String request) {
    String url = "core/2.0/reservations/route-bulk";
    RequestSpecification spec = createAuthenticatedRequest()
        .body(request);
    return doPut("Core - Bulk Add Reservation to Route", spec, url);
  }

  public Response zonalRoutingEditRouteAndGetRawResponse(List<RouteRequest> request) {
    String url = "core/routes";
    String json = toJson(request);
    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);
    return doPut("Core - Zonal Routing Edit Route", spec, url);
  }

  public List<RouteResponse> zonalRoutingEditRoute(List<RouteRequest> request) {
    Response r = zonalRoutingEditRouteAndGetRawResponse(request);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJsonToList(r.body().asString(),
        RouteResponse.class);
  }

  public List<RouteResponse> getUnarchivedRouteDetailsByDriverId(long driverId) {
    String url = "route-v2/routes";
    RequestSpecification spec = createAuthenticatedRequest()
        .queryParam("driver_ids", driverId)
        .queryParam("archived", false);
    Response r = doGet("Route - Get Route Details", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJsonSnakeCase(r.body().asString(),
        GetRouteDetailsResponse.class).getData();
  }

  public List<RouteResponse> getUnarchivedRouteDetailsByDriverId(long driverId, int pageSize,
      long lastId, boolean archived) {
    String url = "route-v2/routes";
    RequestSpecification spec = createAuthenticatedRequest()
        .queryParam("driver_ids", driverId)
        .queryParam("archived", archived)
        .queryParam("page_size", pageSize)
        .queryParam("last_id", lastId);
    Response r = doGet("Route - Get Route Details", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return fromJsonSnakeCase(r.body().asString(),
        GetRouteDetailsResponse.class).getData();
  }

  public Response pullReservationOutOfRouteAndGetRawResponse(long reservationId) {
    String url = "core/2.0/reservations/{reservationId}/unroute";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("reservationId", reservationId)
        .body("{}");
    return doPut("Core - Pull Reservation Out of Route", spec, url);
  }

  public void pullReservationOutOfRoute(long reservationId) {
    Response r = pullReservationOutOfRouteAndGetRawResponse(reservationId);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().assertThat().body(equalTo(f("{\"id\":%d,\"status\":\"PENDING\"}", reservationId)));
  }

  public MergeWaypointsResponse mergeWaypointsZonalRouting(List<Long> waypointIds) {
    String apiMethod = "route-v2/waypoints/merge";
    String json = toJsonSnakeCase(waypointIds);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(f("{\"waypoint_ids\":%s}", json));

    Response response = doPut("Route V2 - Zonal Routing Merge Waypoints",
        requestSpecification,
        apiMethod);
    response.then().assertThat().contentType(ContentType.JSON);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
    return fromJsonSnakeCase(response.body().asString(),
        MergeWaypointsResponse.class);
  }

  public void mergeWaypointsRouteLogs(List<Long> routeIds) {
    String apiMethod = "route-v2/routes/merge-waypoints";
    String json = toJsonSnakeCase(routeIds);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(f("{\"route_ids\":%s}", json));

    Response response = doPut("Route V2 - Route Logs Merge Waypoints",
        requestSpecification,
        apiMethod);
    response.then().assertThat().contentType(ContentType.JSON);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }

  public void addToRouteDp(long orderId, long routeId) {
    String url = "core/2.0/orders/{orderId}/routes-dp";
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"type\":\"%s\",\"route_id\":%s}", "DELIVERY", routeId));
    Response response = doPut("API Core - Add To Route DP", requestSpecification, url);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }

  public void pullOutDpOrderFromRoute(long orderId) {
    String url = "core/2.0/orders/{orderId}/routes-dp";
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("orderId", orderId)
        .body(f("{\"type\":\"DELIVERY\"}"));
    Response response = doDelete("API Core - Pull Out DP Order Waypoint From Route",
        requestSpecification, url);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }

  public Response getGraphqlRouteDetails(Long routeId) {
    final String uri = "core/graphql/route";
    String routeRequest = f(
        "{\"query\": \"query ($id: Int!) {route (id: $id){id, hubId, date, driverId}}\",\"variables\": { \"id\": %s}}",
        routeId);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(routeRequest);
    final Response r = doPost(f("ROUTE SEARCH - GET DETAILS OF ROUTE ID %d", routeId),
        requestSpecification, uri);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return r;
  }

  public void addReservationToRouteGroup(long routeGroupId, long reservationId) {
    addReservationsToRouteGroup(routeGroupId, reservationId);
  }

  public void addReservationsToRouteGroup(long routeGroupId, long... reservationId) {
    String apiMethod = "route/1.0/route-groups/{routeGroupId}/references";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeGroupId", routeGroupId)
        .queryParam("append", true)
        .body(f("{\"transactionIds\":[], \"reservationIds\":%s}", Arrays.toString(reservationId)));

    Response r = doPost("Route - Add Reservation(s) to Route Group", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void addTransactionToRouteGroup(long routeGroupId, long transactionId) {
    addTransactionsToRouteGroup(routeGroupId, transactionId);
  }

  public void addTransactionsToRouteGroup(long routeGroupId, Long... transactionIds) {
    String apiMethod = "route/1.0/route-groups/{routeGroupId}/references";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeGroupId", routeGroupId)
        .queryParam("append", true)
        .body(f("{\"transactionIds\":%s, \"reservationIds\":[]}", Arrays.toString(transactionIds)));

    Response r = doPost("Route - Add Transaction(s) to Route Group", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void deleteRoute(long routeId) {
    Response r = deleteRouteAndGetRawResponse(routeId);
    r.then().assertThat().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().assertThat().body(equalTo(f("[%d]", routeId)));
  }

  public Response deleteRouteAndGetRawResponse(long routeId) {
    String url = "core/routes";

    RequestSpecification spec = createAuthenticatedRequest()
        .body(f("[{\"id\":%d}]", routeId));

    return doDelete("Core - Delete Route", spec, url);
  }

  public List<RouteGroup> getRouteGroups() {
    String apiMethod = "route/1.0/route-groups";
    RequestSpecification requestSpecification = createAuthenticatedRequest();
    Response r = doGet("Route - Get Route Groups", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return r.body().jsonPath().getList("data.routeGroups", RouteGroup.class);
  }

  public void deleteRouteGroup(long routeGroupId) {
    String apiMethod = "route/1.0/route-groups/{routeGroupId}";
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeGroupId", routeGroupId);
    Response r = doDelete("Route - Delete Route Group", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public RouteGroup createRouteGroup(RouteGroup request) {
    String apiMethod = "route/1.0/route-groups";
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(request);
    Response r = doPost("Route - Create Route Group", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    return r.getBody().jsonPath().getObject("data.routeGroup", RouteGroup.class);
  }

  public void addParcelToRouteByTrackingId(AddParcelToRouteRequest addParcelToRouteRequest) {
    final Response response = addParcelToRouteByTrackingIdAndGetRawResponse(
        addParcelToRouteRequest);
    response.then().contentType(ContentType.JSON);
  }

  public void editRouteDetails(List<RouteRequest> routeRequest) {
    String url = "core/routes/details";
    String json = toJson(routeRequest);
    RequestSpecification rs = createAuthenticatedRequest()
        .body(json);
    Response response = doPut("API Core - Operator edit route details:", rs, url);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
  }

  public Response addParcelToRouteByTrackingIdAndGetRawResponse(AddParcelToRouteRequest
      addParcelToRouteRequest) {
    final String url = "core/2.0/orders/routes";
    final String json = toJson(DEFAULT_SNAKE_CASE_MAPPER, addParcelToRouteRequest);

    final RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    return doPut("Core - Add Parcel to Route by Tracking Id", spec, url);
  }

  public void startRoute(long routeId) {
    Response r = startRouteAndGetRawResponse(routeId);
    r.then().statusCode(
        isOneOf(HttpConstants.RESPONSE_200_SUCCESS, HttpConstants.RESPONSE_204_NO_CONTENT));
  }

  public Response startRouteAndGetRawResponse(long routeId) {
    String apiMethod = "core/v2/drivers/1/routes/{routeId}/start";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("routeId", routeId);

    return doGet("API Core - Start Route", requestSpecification, apiMethod);
  }

  public void setRouteTags(long routeId, long tagId) {
    String apiMethod = "route/1.0/routes/tags";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(f("[{\"routeId\":%d,\"tagIds\":[%d]}]", routeId, tagId));

    Response r = doPut("Route - Set Route Tags", requestSpecification, apiMethod);
    r.then().assertThat().contentType(ContentType.JSON);
    r.then().assertThat().statusCode(HttpConstants.RESPONSE_200_SUCCESS);
  }

  public void unmergeTransactionsZonalRouting(List<Long> transactionIds) {
    String apiMethod = "core/transactions/unmerge";
    String json = toJsonSnakeCase(transactionIds);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(f("{\"transactionIds\":%s}", json));

    Response response = doPut("Core - Unmerge Transactions", requestSpecification,
        apiMethod);
    response.then().assertThat().contentType(ContentType.JSON);
    response.then().assertThat().statusCode(HttpConstants.RESPONSE_200_SUCCESS);
  }

  public long getDeliveryWaypointId(String trackingId) {
    Response response = getDeliveryWaypointIdAsRawResponse(trackingId);
    response.then().assertThat().statusCode(HttpConstants.RESPONSE_200_SUCCESS);
    response.then().assertThat().contentType(ContentType.JSON);
    return response.jsonPath().<Integer>get("delivery.waypointId");
  }

  public Response getDeliveryWaypointIdAsRawResponse(String trackingId) {
    String apiMethod = "core/orders/search";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .param("scan", trackingId);

    return doGet("API Core - Get Delivery Waypoint ID", requestSpecification, apiMethod);
  }

  public CreateCoverageResponse createCoverage(CreateCoverageRequest request) {
    String url = "route-v2/coverages";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(toJsonSnakeCase(request));

    Response response = doPost("API Route - Create Coverage", requestSpecification, url);
    return fromJsonSnakeCase(response.getBody().asString(), CreateCoverageResponse.class);
  }

  public void deleteCoverage(long coverageId) {
    String url = "route-v2/coverages/{coverageId}";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("coverageId", coverageId);

    doDelete("API Route - Delete Coverage", requestSpecification, url);
  }

  public Response parcelRouteTransferAndGetRawResponse(ParcelRouteTransferRequest request) {
    String url = "core/2.0/routes-orders-transfers";
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(toJsonSnakeCase(request));
    return doPost("Parcel Route Transfer", requestSpecification, url);
  }

  public ParcelRouteTransferResponse parcelRouteTransfer(ParcelRouteTransferRequest request) {
    Response response = parcelRouteTransferAndGetRawResponse(request);
    if (response.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + response.statusCode());
    }
    return fromJsonSnakeCase(response.body().asString(), ParcelRouteTransferResponse.class);
  }

  public RouteResponse getRouteDetails(long routeId) {
    String apiMethod = "route/2.0/routes/{route-id}/manifest";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("route-id", routeId);

    Response r = doGet(f("API Route - Get Route ID %d Details", routeId),
        requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return convertValueCamelCase(r.body().as(JsonNode.class).get("data").get("route"),
        RouteResponse.class);
  }

  public void deleteDriverTypeRule(long driverTypeId) {
    String url = "route/rbe/rules/drivertype/{driver_type_id}";
    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("driver_type_id", driverTypeId);

    Response r = doDelete("Delete Driver Type Rule", spec, url);
    r.then().contentType(ContentType.JSON);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public List<MilkRunGroup> getMilkrunGroups(Date date) {
    String apiMethod = "route/1.0/milkrun-groups";
    String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .queryParam("date", dateStr);

    Response r = doGet("API Route - Get Milkrun Groups", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return r.body().jsonPath().getList("data", MilkRunGroup.class);
  }

  public void deleteMilkrunGroup(long groupId) {
    String apiMethod = "route/1.0/milkrun-groups/{group-id}";
    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("group-id", groupId);

    Response r = doDelete("API Route - Delete Milkrun Group", requestSpecification,
        apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
  }

  public void forceFailWaypoint(long routeId, long waypointId, long failureReasonId) {
    String url = "core/admin/routes/{routeId}/waypoints/{waypointId}/pods";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body(f("{ \"action\": \"fail\", \"failure_reason_id\":%d}", failureReasonId));

    Response r = doPut("Core - Admin Force Fail", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void forceSuccessWaypoint(long routeId, long waypointId) {
    String url = "core/admin/routes/{routeId}/waypoints/{waypointId}/pods";

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body("{ \"action\": \"success\"}");

    Response r = doPut("Core - Admin Force Success", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void forceSuccessWaypointWithCodCollected(long routeId, long waypointId,
      List<Long> orderIds) {
    String url = "core/admin/routes/{routeId}/waypoints/{waypointId}/pods";
    String json = f("{ \"action\": \"success\", \"cod_collected_order_ids\": %s}", orderIds);

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .pathParam("waypointId", waypointId)
        .body(json);

    Response r = doPut("Core - Admin Force Success with COD collected", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void runFmAutoRouteCronJob(String date) {
    String url = "route-v2/debug/reservations/auto-routing";
    String json = f("{ \"date\": \"%s\", \"execute\": true}", date);

    RequestSpecification spec = createAuthenticatedRequest()
        .body(json);

    Response r = doPost("Route - Run FM Routing Cron Job", spec, url);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public void addMultipleWaypointsToRoute(long routeId, List<Long> waypointIds) {
    Response r = addMultipleWaypointsToRouteAndGetRawResponse(routeId, waypointIds);
    if (r.statusCode() != HttpConstants.RESPONSE_204_NO_CONTENT) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
  }

  public Response addMultipleWaypointsToRouteAndGetRawResponse(long routeId,
      List<Long> waypointIds) {
    String url = "route-v2/routes/{routeId}/waypoints";
    String json = f("{\"waypoint_ids\": %s}", toJsonSnakeCase(waypointIds));

    RequestSpecification spec = createAuthenticatedRequest()
        .pathParam("routeId", routeId)
        .body(json);
    return doPut("Route - Add Multiple Waypoints to Route", spec, url);
  }
}
