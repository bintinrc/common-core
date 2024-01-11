package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.client.TagClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.exception.NvTestCoreCastingErrorException;
import co.nvqa.common.core.exception.NvTestCoreMilkrunGroupNotFoundException;
import co.nvqa.common.core.model.RouteGroup;
import co.nvqa.common.core.model.coverage.CreateCoverageRequest;
import co.nvqa.common.core.model.coverage.CreateCoverageResponse;
import co.nvqa.common.core.model.other.CoreExceptionResponse;
import co.nvqa.common.core.model.other.CoreExceptionResponse.Error;
import co.nvqa.common.core.model.pickup.MilkRunGroup;
import co.nvqa.common.core.model.reservation.BulkRouteReservationResponse;
import co.nvqa.common.core.model.route.AddParcelToRouteRequest;
import co.nvqa.common.core.model.route.AddPickupJobToRouteRequest;
import co.nvqa.common.core.model.route.BulkAddPickupJobToRouteRequest;
import co.nvqa.common.core.model.route.BulkAddPickupJobToRouteResponse;
import co.nvqa.common.core.model.route.EditRouteRequest;
import co.nvqa.common.core.model.route.MergeWaypointsResponse;
import co.nvqa.common.core.model.route.MergeWaypointsResponse.Data;
import co.nvqa.common.core.model.route.ParcelRouteTransferRequest;
import co.nvqa.common.core.model.route.ParcelRouteTransferResponse;
import co.nvqa.common.core.model.route.RouteRequest;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.core.model.route.RouteTag;
import co.nvqa.common.core.model.route.TagResponse;
import co.nvqa.common.core.model.waypoint.Waypoint;
import co.nvqa.common.core.utils.CoreTestUtils;
import co.nvqa.common.model.DataEntity;
import co.nvqa.common.utils.StandardTestUtils;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class ApiRouteSteps extends CoreStandardSteps {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiRouteSteps.class);

  @Inject
  @Getter
  private RouteClient routeClient;

  @Inject
  @Getter
  private TagClient tagClient;


  /**
   * Sample:
   * <p>
   * Given API Operator create new route using data below: | createRouteRequest | {
   * "zoneId":{zone-id}, "hubId":{hub-id}, "vehicleId":{vehicle-id}, "driverId":{ninja-driver-id} }
   * |
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Core - Operator create new route using data below:")
  public void apiOperatorCreateNewRouteUsingDataBelow(Map<String, String> dataTableAsMap) {
    doWithRetry(() -> {
      final RouteResponse createRouteResponse = getRouteClient()
          .createRoute(generateRouteRequest(dataTableAsMap));
      putInList(KEY_LIST_OF_CREATED_ROUTES, createRouteResponse);
      putInList(KEY_LIST_OF_CREATED_ROUTE_ID, createRouteResponse.getId());
      put(KEY_CREATED_ROUTE_ID, createRouteResponse.getId());
    }, "create route");
  }

  @Given("API Core - Operator create new route from zonal routing using data below:")
  public void apiOperatorCreateNewRouteFromZonalRoutingUsingDataBelow(
      Map<String, String> dataTableAsMap) {
    apiOperatorCreateNewRouteUsingDataBelow(dataTableAsMap);
  }

  @Given("API Core - Operator fail to create new route from zonal routing using data below:")
  public void apiOperatorFailCreateRouteFromZonalRouting(
      Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    doWithRetry(() -> {
      final Response createRouteResponse = getRouteClient()
          .createRouteAndGetRawResponse(generateRouteRequest(dataTableAsMap));
      Assertions.assertThat(createRouteResponse.getStatusCode()).as("status code")
          .isEqualTo(HttpConstants.RESPONSE_500_INTERNAL_SERVER_ERROR);
      Error actual = fromJsonSnakeCase(createRouteResponse.getBody().asString(), Error.class);
      Error expected = fromJsonSnakeCase(resolvedDataTable.get("errorMessage"), Error.class);
      expected.compareWithActual(actual, resolvedDataTable);
    }, "failed to create route");
  }

  /**
   * Sample:<p>
   * <p>
   * When API Operator add parcel to the route using data below:<p> | orderId | 111111 |<p> |
   * addParcelToRouteRequest | {"route_id":95139463,"type":"DELIVERY"} |<p>
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Core - Operator add parcel to the route using data below:")
  public void apiOperatorAddParcelToTheRouteUsingDataBelow(Map<String, String> dataTableAsMap) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final String addParcelToRouteRequestTemplate = resolvedDataTable.get("addParcelToRouteRequest");
    final long orderId = Long.parseLong(resolvedDataTable.get("orderId"));
    final AddParcelToRouteRequest addParcelToRouteRequest = fromJsonSnakeCase(
        addParcelToRouteRequestTemplate, AddParcelToRouteRequest.class);
    doWithRetry(
        () -> getRouteClient().addParcelToRoute(orderId, addParcelToRouteRequest),
        "add order to route");
  }

  /**
   * Sample:<p>
   * <p>
   * And API Core - Operator add multiple parcels to route "{KEY_LIST_OF_CREATED_ROUTES[1].id}" with
   * type "DELIVERY" using data below:<br> | {KEY_LIST_OF_CREATED_ORDERS[1].id} |<br> |
   * {KEY_LIST_OF_CREATED_ORDERS[2].id} |<br> | {KEY_LIST_OF_CREATED_ORDERS[3].id} |<br> |
   * {KEY_LIST_OF_CREATED_ORDERS[4].id} |<br>
   * <p>
   */
  @Given("API Core - Operator add multiple parcels to route {string} with type {string} using data below:")
  public void apiOperatorAddMultipleParcelToTheRouteUsingDataBelow(
      String routeId, String routeType, List<String> orderIds) {

    String resolvedRouteId = resolveValue(routeId);
    List<String> resolvedOrderIds = resolveValues(orderIds);

    AddParcelToRouteRequest addParcelToRouteRequest = new AddParcelToRouteRequest();
    addParcelToRouteRequest.setRouteId(Long.valueOf(resolvedRouteId));
    addParcelToRouteRequest.setType(routeType);

    resolvedOrderIds.forEach(orderId -> doWithRetry(
        () -> getRouteClient().addParcelToRoute(Long.parseLong(orderId), addParcelToRouteRequest),
        "add order to route"));
  }

  /**
   * Sample:<p>
   * <p>
   * When API Core - Operator add pickup job to the route using data below:<p> | jobId | 1111 |<p> |
   * addPickupJobToRouteRequest | {"new_route_id":95682687,"overwrite":false} |<p>
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Core - Operator add pickup job to the route using data below:")
  public void apiOperatorAddPickupJobToTheRouteUsingDataBelow(Map<String, String> dataTableAsMap) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final String addPickupJobToRouteRequestTemplate = resolvedDataTable
        .get("addPickupJobToRouteRequest");
    final long jobId = Long.parseLong(resolvedDataTable.get("jobId"));
    final AddPickupJobToRouteRequest addPickupJobToRouteRequest = fromJsonSnakeCase(
        addPickupJobToRouteRequestTemplate, AddPickupJobToRouteRequest.class);
    doWithRetry(
        () -> getRouteClient().addPickupJobToRoute(jobId, addPickupJobToRouteRequest)
        , "add pa job to route");
  }

  @Given("API Core - Operator bulk add pickup jobs to the route using data below:")
  public void apiOperatorBulkAddPickupJobToTheRouteUsingDataBelow(
      Map<String, String> dataTableAsMap) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final String bulkAddPickupJobToTheRouteRequestTemplate = resolvedDataTable
        .get("bulkAddPickupJobToTheRouteRequest");

    final BulkAddPickupJobToRouteRequest bulkAddPickupJobToRouteRequest;
    try {
      bulkAddPickupJobToRouteRequest = fromJsonSnakeCase(
          bulkAddPickupJobToTheRouteRequestTemplate, BulkAddPickupJobToRouteRequest.class);
    } catch (Exception e) {
      LOGGER.error("bulkAddPickupJobToTheRouteRequest: {}",
          bulkAddPickupJobToTheRouteRequestTemplate);
      throw new NvTestCoreCastingErrorException(
          "error casting bulkAddPickupJobToTheRouteRequest to BulkAddPickupJobToRouteRequest.class",
          e);
    }

    doWithRetry(() -> {
      BulkAddPickupJobToRouteResponse response = getRouteClient().bulkAddPickupJobToRoute(
          bulkAddPickupJobToRouteRequest);
      put(KEY_CORE_BULK_ROUTE_PA_JOB_RESPONSE, response);
    }, "Operator Bulk Add Pickup Jobs to Route", 2000, 3);
  }

  @Given("API Core - Operator remove pickup job id {string} from route")
  public void apiOperatorRemovePickupJobFromRouteUsingDataBelow(String paJobId) {
    final long jobId = Long.parseLong(resolveValue(paJobId));
    doWithRetry(
        () -> getRouteClient().removePAJobFromRoute(jobId),
        "remove pa job from route");
  }

  @Given("API Core - Operator update routed waypoint to pending")
  public void operatorUpdateWaypointToPending(Map<String, String> dataTableAsMap) {
    final String json = toJsonCamelCase(resolveKeyValues(dataTableAsMap));
    final Waypoint waypoint = fromJsonCamelCase(json, Waypoint.class);
    final List<Waypoint> request = Collections.singletonList(waypoint);
    doWithRetry(
        () -> getRouteClient().updateWaypointToPending(request),
        "set routed waypoint to pending");
  }

  /**
   * Sample:<p>
   * <p>
   * Given API Core - Operator archives routes below:<p> | {KEY_LIST_OF_CREATED_ROUTES[1].id} |
   * <p> | {KEY_LIST_OF_CREATED_ROUTES[2].id} | <p>
   *
   * @param routeIds
   */
  @When("API Core - Operator archives routes below:")
  public void operatorArchivesRoutes(List<String> routeIds) {
    routeIds = resolveValues(routeIds);
    List<Long> ids = routeIds.stream().map(Long::parseLong).collect(Collectors.toList());
    doWithRetry(
        () -> getRouteClient().archiveRoutes(ids),
        "Archive route", 1000, 5);
  }

  @When("API Core - Operator unarchives routes below:")
  public void operatorUnarchivesRoutes(List<String> routeIds) {
    routeIds = resolveValues(routeIds);
    List<Long> ids = routeIds.stream().map(Long::parseLong).collect(Collectors.toList());
    doWithRetry(
        () -> getRouteClient().unarchiveRoutes(ids),
        "Unarchive route", 1000, 5);
  }

  @When("API Core - Operator unarchives invalid route with data below:")
  public void operatorUnArchiveRouteInvalidState(Map<String, String> mapOfData) {
    Map<String, String> expectedData = resolveKeyValues(mapOfData);
    final long routeId = Long.parseLong(expectedData.get("routeId"));
    doWithRetry(
        () -> {
          Response response = getRouteClient().unarchiveRouteAndGetRawResponse(routeId);
          Assertions.assertThat(response.getStatusCode()).as("status code")
              .isEqualTo(Integer.valueOf(expectedData.get("status")));
          put(KEY_ROUTE_RESPONSE, response);
        },
        "Unarchive route", 1000, 5);
  }

  @When("API Core - Operator archives invalid route with data below:")
  public void operatorArchiveRouteInvalidState(Map<String, String> mapOfData) {
    Map<String, String> expectedData = resolveKeyValues(mapOfData);
    final long routeId = Long.parseLong(expectedData.get("routeId"));
    doWithRetry(
        () -> {
          Response response = getRouteClient().archiveRouteAndGetRawResponse(routeId);
          Assertions.assertThat(response.getStatusCode()).as("status code")
              .isEqualTo(Integer.valueOf(expectedData.get("status")));
          put(KEY_ROUTE_RESPONSE, response);
        },
        "Unarchive route", 1000, 5);
  }

  /**
   * Sample <p> API Core - Operator pull order from route: <p> | orderId |
   * {KEY_LIST_OF_CREATED_ORDERS[1].id} | <p>| type    | DELIVERY or PICKUP | <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator pull order from route:")
  public void operatorPullOrderFromRoute(Map<String, String> dataTableAsMap) {
    final Map<String, String> map = resolveKeyValues(dataTableAsMap);
    final long orderId = Long.parseLong(map.get("orderId"));
    final String type = map.get("type");
    doWithRetry(
        () -> getRouteClient().pullFromRoute(orderId, type),
        "Operator pull order from route");
  }

  @When("API Core - Operator Edit Route Waypoint on Zonal Routing Edit Route:")
  public void editRouteZonalRouting(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    String createRouteRequestJson = StandardTestUtils
        .replaceTokens(resolvedDataTable.get("editRouteRequest"),
            StandardTestUtils.createDefaultTokens());
    List<RouteRequest> request = fromJsonToList(createRouteRequestJson, RouteRequest.class);
    doWithRetry(
        () -> {
          final List<RouteResponse> route = getRouteClient()
              .zonalRoutingEditRoute(request);
          Assertions.assertThat(route.get(0)).as("updated route is not null").isNotNull();
        },
        "Zonal Routing Edit Route");
  }

  /**
   * Sample:<p>
   * <p>
   * When API Operator add reservation pick-ups to the route using data below:<p> | reservationId |
   * 111111 |<p> | routeId       | 222222 |<p> |overwrite|true|
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator add reservation to route using data below:")
  public void apiOperatorAddReservationPickUpsToTheRoute(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final long reservationResultId = Long.parseLong(resolvedDataTable.get("reservationId"));
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));

    final Optional<Boolean> overwriteParam = dataTableAsMap.containsKey("overwrite") ?
        Optional.of(Boolean.valueOf(dataTableAsMap.get("overwrite"))) : Optional.of(true);
    final Boolean overwrite = overwriteParam.get();

    doWithRetry(
        () -> getRouteClient().addReservationToRoute(routeId, reservationResultId, overwrite),
        "add reservation to route with overwrite: true");
  }

  @When("API Core - Operator failed to add reservation to route using data below:")
  public void apiOperatorFailedAddReservationPickUpsToTheRoute(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final long reservationId = Long.parseLong(resolvedDataTable.get("reservationId"));
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
    final Optional<Boolean> overwriteParam = dataTableAsMap.containsKey("overwrite") ?
        Optional.of(Boolean.valueOf(dataTableAsMap.get("overwrite"))) : Optional.of(true);
    final Boolean overwrite = overwriteParam.get();

    final int expectedStatusCode = Integer.parseInt(resolvedDataTable.get("expectedStatusCode"));
    final String expectedErrorMessage = resolvedDataTable.get("expectedErrorMessage");

    doWithRetry(() -> {
      Response r = getRouteClient().addReservationToRouteAndGetRawResponse(routeId,
          reservationId, overwrite);

      Assertions.assertThat(r.statusCode())
          .as("expected http status: " + r.statusCode())
          .isEqualTo(expectedStatusCode);

      Assertions.assertThat(r.getBody().asString())
          .as("expected error message: " + r.getBody().asString())
          .isEqualTo(expectedErrorMessage);
    }, "(expected) failed add reservation to route");
  }

  /**
   * Sample:<p>
   * <p>
   * API Core - Operator remove reservation id {KEY_LIST_OF_CREATED_RESERVATIONS[1].id} from route
   * <p>
   */
  @When("API Core - Operator remove reservation id {string} from route")
  public void apiOperatorAddReservationPickUpsToTheRoute(String id) {
    final long reservationResultId = Long.parseLong(resolveValue(id));
    doWithRetry(
        () -> getRouteClient().pullReservationOutOfRoute(reservationResultId),
        "remove reservation from route ");
  }

  @When("API Core - Operator failed to remove reservation id {string} from route")
  public void apiOperatorFailedToAddReservationPickUpsToTheRoute(String reservationId,
      Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final long reservationResultId = Long.parseLong(resolveValue(reservationId));
    final int expectedStatusCode = Integer.parseInt(resolvedDataTable.get("expectedStatusCode"));
    final String expectedErrorMessage = resolvedDataTable.get("expectedErrorMessage");

    doWithRetry(() -> {
      Response r = getRouteClient().pullReservationOutOfRouteAndGetRawResponse(
          reservationResultId);

      Assertions.assertThat(r.statusCode())
          .as("expected http status: " + r.statusCode())
          .isEqualTo(expectedStatusCode);

      Assertions.assertThat(r.getBody().asString())
          .as("expected error message: " + r.getBody().asString())
          .isEqualTo(expectedErrorMessage);
    }, "(expected) failed add reservation to route");
  }

  /**
   * Sample:<p>
   * <p>
   * When API Core - Operator bulk add reservation to route using data below: | request | {"ids":
   * [{KEY_LIST_OF_CREATED_RESERVATIONS[1].id},
   * {KEY_LIST_OF_CREATED_RESERVATIONS[2].id}],"new_route_id":{KEY_LIST_OF_CREATED_ROUTES[1].id},"overwrite":true}
   * |
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator bulk add reservation to route using data below:")
  public void apiOperatorBulkAddReservationToRoute(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final String request = resolvedDataTable.get("request");
    doWithRetry(
        () -> {
          BulkRouteReservationResponse response = getRouteClient()
              .bulkAddToRouteReservation(request);
          put(KEY_ROUTE_RESPONSE, response);
        },
        "bulk add reservation to route ");
  }

  /**
   * Sample:<p>
   * <p>
   * When API Core - Operator bulk add reservation to route with partial success: | request |
   * {"ids": [{KEY_LIST_OF_CREATED_RESERVATIONS[1].id},
   * {KEY_LIST_OF_CREATED_RESERVATIONS[2].id}],"new_route_id":{KEY_LIST_OF_CREATED_ROUTES[1].id},"overwrite":true}
   * |
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator bulk add reservation to route with partial success:")
  public void apiOperatorBulkAddReservationToRoutePartial(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final String request = resolvedDataTable.get("request");
    doWithRetry(
        () -> {
          Response r = getRouteClient().bulkAddToRouteReservationAndGetRawResponse(request);
          Assertions.assertThat(r.getStatusCode()).as("status code Message")
              .isEqualTo(HttpConstants.RESPONSE_400_BAD_REQUEST);
          Error actual = fromJsonSnakeCase(r.getBody().asString(), Error.class);
          Error expected = fromJsonSnakeCase(resolvedDataTable.get("failedJobs"), Error.class);
          expected.compareWithActual(actual, resolvedDataTable);
        },
        "bulk add reservation to route ");
  }

  @Given("API Core - Operator merge waypoints on Zonal Routing:")
  public void apiOperatorMergeWaypoints(List<String> waypointIds) {
    waypointIds = resolveValues(waypointIds);
    List<Long> wpIds = waypointIds.stream().map(Long::parseLong).collect(Collectors.toList());
    doWithRetry(
        () -> {
          final MergeWaypointsResponse result = getRouteClient().mergeWaypointsZonalRouting(wpIds);
          put(KEY_CORE_MERGE_WAYPOINT_RESPONSE, toJsonSnakeCase(result));
        },
        "merge waypoints on zonal routing");
  }

  /**
   * Sample:<p>
   * <p>
   * When API Core - Operator merge routed waypoints: |{KEY_LIST_OF_CREATED_ROUTES[1].id}|
   * |{KEY_LIST_OF_CREATED_ROUTES[2].id}|
   * <p>
   */
  @Given("API Core - Operator merge routed waypoints:")
  public void apiOperatorMergeRoutedWaypoints(List<String> routeIds) {
    routeIds = resolveValues(routeIds);
    List<Long> resolvedRouteIds = routeIds.stream().map(Long::parseLong)
        .collect(Collectors.toList());
    doWithRetry(
        () -> getRouteClient().mergeWaypointsRouteLogs(resolvedRouteIds),
        "merge waypoints on route logs");
  }

  @When("API Core - Operator verifies response of merge waypoint on Zonal Routing")
  public void verifyMergeWaypointResponse(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    List<Data> expected = fromJson(resolvedDataTable.get("expectedResponse"),
        MergeWaypointsResponse.class).getData();
    List<Data> actual =
        fromJson(resolvedDataTable.get("actualResponse"), MergeWaypointsResponse.class).getData();
    Assertions.assertThat(actual)
        .as("merge waypoints response is not null")
        .isNotNull();
    Assertions.assertThat(actual.size())
        .as("merge waypoints response size match")
        .isEqualTo(expected.size());
    expected.forEach(o -> DataEntity.assertListContains(actual, o, "merged waypoints list"));
  }

  //  DO NOT use this to add to route for normal order (non-DP order)

  /**
   * When API Core - Operator new add parcel to DP holding route: | orderId |
   * {KEY_LIST_OF_CREATED_ORDERS[1].id} | | routeId | {KEY_LIST_OF_CREATED_ROUTES[1].id} |
   */
  @Given("API Core - Operator new add parcel to DP holding route:")
  public void operatorAddToDpHoldingRoute(Map<String, String> data) {
    data = resolveKeyValues(data);
    final long routeId = Long.parseLong(data.get("routeId"));
    final long orderId = Long.parseLong(data.get("orderId"));
    doWithRetry(
        () -> getRouteClient().addToRouteDp(orderId, routeId),
        "Add to route dp order");
  }

  @Given("API Core - Operator Get Delivery Waypoint ID for tracking ID {string}")
  public void operatorGetDeliveryWaypointId(String tid) {
    String resolvedTid = resolveValue(tid);
    doWithRetry(
        () -> {
          Long wayPointId = getRouteClient().getDeliveryWaypointId(resolvedTid);
          put(KEY_WAYPOINT_ID, wayPointId);
        },
        "Get delivery waypoint id");
  }
  //  DO NOT use this to pull order out from route for normal order (non-DP order)

  /**
   * When API Core - Operator pull out dp order from DP holding route for order |
   * {KEY_LIST_OF_CREATED_ORDERS[1].id} |
   */
  @Given("API Core - Operator pull out dp order from DP holding route for order")
  public void operatorPullOutDpOrder(List<String> data) {
    doWithRetry(
        () -> data.forEach(
            e -> getRouteClient().pullOutDpOrderFromRoute(Long.parseLong(resolveValue(e)))),
        "pull out dp order from route");
  }

  /**
   * <b>Example datatable</b>
   * <ul>
   * <b>When API Core - Operator edit route details:</b>
   * <p>
   * | editRouteRequest | [ { "id": {KEY_LIST_OF_CREATED_ROUTES[1].id}, "driverId": {ninja-driver-id}, "hubId": {hub-id}, "tags": [ {tag-ids} ], "zoneId": {zone-id}, "date":"{date: 0 days next, yyyy-MM-dd}T16:00:00Z", "datetime": "{date: 0 days next, yyyy-MM-dd} 16:00:00" } ] |
   * </p>
   * </ul>
   */
  @When("API Core - Operator edit route details:")
  public void operatorEditRouteDetails(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    String editRouteRequestJson = StandardTestUtils
        .replaceTokens(resolvedDataTable.get("editRouteRequest"),
            StandardTestUtils.createDefaultTokens());
    List<EditRouteRequest> editRouteRequest = fromJsonToList(editRouteRequestJson,
        EditRouteRequest.class);

    doWithRetry(
        () -> getRouteClient().editRouteDetails(editRouteRequest), "Edit Route Details");
  }

  /**
   * Sample:<p>
   * <p>
   * API Core - Operator force success waypoint via route manifest: | routeId    |
   * {KEY_LIST_OF_CREATED_ROUTES[1].id}               | | waypointId |
   * {KEY_CORE_LIST_OF_RESERVATIONS_DB[1].waypointId} |
   */
  @Given("API Core - Operator force success waypoint via route manifest:")
  public void apiOperatorForceSuccessRouteManifest(Map<String, String> dataTableAsMap) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
    final long waypointId = Long.parseLong(resolvedDataTable.get("waypointId"));
    doWithRetry(
        () -> getRouteClient().forceSuccessWaypoint(routeId, waypointId),
        "force success route manifest");
  }

  /**
   * Sample:<p>
   * <p>
   * API Core - Operator force success waypoint with cod collected as {string} using route manifest
   * |routeId    | {KEY_LIST_OF_CREATED_ROUTES[1].id}                                     |
   * |waypointId |{KEY_CORE_LIST_OF_RESERVATIONS_DB[1].waypointId}                        |
   * |orderIds   | {KEY_LIST_OF_CREATED_ORDERS[1].id},{KEY_LIST_OF_CREATED_ORDERS[2].id} |
   */
  @Given("API Core - Operator force success waypoint with cod collected as {string} using route manifest:")
  public void apiOperatorForceSuccessRouteManifest(String codCollected,
      Map<String, String> dataTableAsMap) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
    final long waypointId = Long.parseLong(resolvedDataTable.get("waypointId"));
    final List<Long> orderIds = Arrays.stream(resolvedDataTable.get("orderIds").split(","))
        .map(Long::parseLong).collect(Collectors.toCollection(ArrayList::new));
    doWithRetry(
        () -> {
          if (Boolean.parseBoolean(codCollected)) {
            getRouteClient().forceSuccessWaypointWithCodCollected(routeId, waypointId, orderIds);

          } else {
            List<Long> emptyOrderId = new ArrayList<>();
            getRouteClient().forceSuccessWaypointWithCodCollected(routeId, waypointId,
                emptyOrderId);
          }
        }, "force success route manifest");
  }

  /**
   * Sample:<p> API Core - Operator force fail waypoint via route manifest: | routeId         |
   * {KEY_LIST_OF_CREATED_ROUTES[1].id}               | | waypointId      |
   * {KEY_CORE_LIST_OF_RESERVATIONS_DB[1].waypointId} | | failureReasonId | 12345 |
   */
  @Given("API Core - Operator force fail waypoint via route manifest:")
  public void apiOperatorForceFailRouteManifest(Map<String, String> dataTableAsMap) {
    final Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
    final long waypointId = Long.parseLong(resolvedDataTable.get("waypointId"));
    final long failureReasonId = Long.parseLong(resolvedDataTable.get("failureReasonId"));
    doWithRetry(
        () -> getRouteClient().forceFailWaypoint(routeId, waypointId, failureReasonId),
        "force fail route manifest");
  }

  @Given("API Route - Operator archive all unarchived routes of driver id {string}")
  public void archiveAllRoutesOfDriver(String driverId) {
    doWithRetry(
        () -> {
          List<RouteResponse> routes = getRouteClient()
              .getUnarchivedRouteDetailsByDriverId(Long.parseLong(driverId));
          if (!routes.isEmpty()) {
            routes.forEach(e -> getRouteClient().archiveRoute(e.getLegacyId()));
          }
        },
        "archive all routes");
  }

  @Given("API Route - Operator run FM auto route cron job for date {string}")
  public void runFmRoutingCronJob(String date) {
    doWithRetry(
        () -> getRouteClient().runFmAutoRouteCronJob(date),
        "run fm routing cronjob");
  }

  @Given("API Route - Operator run FM PAJ auto route cron job for date {string}")
  public void runFmPajRoutingCronJob(String date) {
    doWithRetry(
        () -> getRouteClient().runFmPajAutoRouteCronJob(date),
        "run fm paj routing cronjob");
  }

  @And("API Route - Operator create new coverage:")
  public void createNewCoverage(Map<String, String> data) {
    CreateCoverageRequest request = new CreateCoverageRequest(resolveKeyValues(data));
    doWithRetry(() -> {
      final CreateCoverageResponse response = getRouteClient().createCoverage(request);
      putInList(KEY_LIST_OF_COVERAGE, response.getData());
    }, "Create new coverage");
  }

  /**
   * Sample: API Core - Operator parcel transfer to a new route: | request |
   * {{"route_id":null,"route_date":"2021-01-19
   * 08:25:13","from_driver_id":null,"to_driver_id":2679,"to_driver_hub_id":3,"orders":[{"tracking_id":"NVSGDIMMI000238068","inbound_type":"VAN_FROM_NINJAVAN","hub_id":3}]|
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Core - Operator parcel transfer to a new route:")
  public void apiOperatorParcelTransfer(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    ZonedDateTime routeDate = CoreTestUtils.getDateForToday();
    String formattedRouteDate = DTF_NORMAL_DATETIME.format(
        routeDate.withZoneSameInstant(ZoneId.of("UTC")));

    ParcelRouteTransferRequest request = fromJsonSnakeCase(resolvedDataTable.get("request"),
        ParcelRouteTransferRequest.class);
    if (request.getRouteDate() == null) {
      request.setRouteDate(formattedRouteDate);
    }

    doWithRetry(() -> {
      final ParcelRouteTransferResponse createRouteResponse = getRouteClient()
          .parcelRouteTransfer(request);
      put(KEY_LIST_OF_CREATED_ROUTES, createRouteResponse.getRoutes());
    }, "parcel route transfer");
  }

  /**
   * Sample:
   * <p>
   * API Route - Operator add multiple waypoints to route: |routeId|123456 |
   * |waypointIds|[1234,4567, 8990]|
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Route - Operator add multiple waypoints to route:")
  public void apiAddMultipleWaypointsToRoute(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
    final List<Long> waypointIds = fromJsonToList(resolvedDataTable.get("waypointIds"), Long.class);
    doWithRetry(() ->
            getRouteClient().addMultipleWaypointsToRoute(routeId, waypointIds),
        "add multiple waypoints to route");
  }

  @Given("API Route - Operator failed to add multiple waypoints to route:")
  public void apiFailedAddMultipleWaypointsToRoute(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
    final int responseCode = Integer.parseInt(resolvedDataTable.get("responseCode"));
    final List<Long> waypointIds = fromJsonToList(resolvedDataTable.get("waypointIds"), Long.class);
    doWithRetry(() ->
        {
          Response response = getRouteClient()
              .addMultipleWaypointsToRouteAndGetRawResponse(routeId, waypointIds);
          Assertions.assertThat(response.getStatusCode()).as("status code Message")
              .isEqualTo(responseCode);
          CoreExceptionResponse actualResponse = fromJsonSnakeCase(response.body().asString(),
              CoreExceptionResponse.class);
          CoreExceptionResponse expectedResponse = fromJsonSnakeCase(
              resolvedDataTable.get("error"), CoreExceptionResponse.class);
          expectedResponse.compareWithActual(actualResponse, resolvedDataTable);
        },
        "add multiple waypoints to route");
  }

  @Given("API Route - add references to Route Group:")
  public void apiOperatorAddTransactionsToRouteGroup(Map<String, String> data) {
    var finalData = resolveKeyValues(data);
    doWithRetry(() -> getRouteClient().addReferencesToRouteGroup(
        Long.parseLong(finalData.get("routeGroupId")),
        finalData.get("requestBody")), "add references to Route Group", 2000, 3);
  }

  /**
   * Sample:
   * <p>
   * API Route - create route group: |name|ARG-{uniqueString} | |description|This Route Group is
   * created by automation test from Operator V2.|
   *
   * @param data Map of data from feature file.
   */
  @Given("API Route - create route group:")
  public void apiOperatorCreateNewRouteGroup(Map<String, String> data) {
    RouteGroup routeGroup = new RouteGroup(resolveKeyValues(data));
    String uniqueString = CoreTestUtils.generateUniqueId();
    if (StringUtils.endsWithIgnoreCase(routeGroup.getName(), "{uniqueString}")) {
      routeGroup.setName(routeGroup.getName().replace("{uniqueString}", uniqueString));
    }
    doWithRetry(() -> {
      var response = getRouteClient().createRouteGroup(routeGroup);
      putInList(KEY_LIST_OF_CREATED_ROUTE_GROUPS, response);
    }, "Create route group", 2000, 3);
  }

  @Given("API Route - delete routes:")
  public void deleteRoutes(List<String> routeIds) {
    routeIds = resolveValues(routeIds);
    routeIds.stream()
        .map(Long::parseLong)
        .forEach(id -> getRouteClient().deleteRoute(id));
  }

  @And("API Route - Operator get created Reservation Group params:")
  public void apiOperatorGetCreatedReservationGroupParams(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    final String reservationGroupName = resolvedDataTable.get("reservationGroupName");
    doWithRetry(() -> {
      List<MilkRunGroup> milkrunGroups = getRouteClient().getMilkrunGroups(new Date());
      MilkRunGroup group = milkrunGroups.stream().filter(
              milkrunGroup -> StringUtils.equals(milkrunGroup.getName(), reservationGroupName))
          .findFirst().orElseThrow(() -> new NvTestCoreMilkrunGroupNotFoundException(
              "Could not find milkrun group with name [" + reservationGroupName + "]"));
      put(KEY_CORE_CREATED_RESERVATION_GROUP_ID, group.getId());
      putInList(KEY_CORE_LIST_OF_CREATED_RESERVATION_GROUP, group);
    }, "Operator get created Reservation Group params");
  }

  private RouteRequest generateRouteRequest(Map<String, String> dataTableAsMap) {
    String scenarioName = getScenarioManager().getCurrentScenario().getName();

    ZonedDateTime routeDate;
    if (dataTableAsMap.containsKey("to_use_different_date")) {
      routeDate = CoreTestUtils.getDateForNextDay();
    } else {
      routeDate = CoreTestUtils.getDateForToday();
    }

    String createdDate = DTF_CREATED_DATE.format(ZonedDateTime.now());
    String formattedRouteDate = DTF_NORMAL_DATETIME.format(
        routeDate.withZoneSameInstant(ZoneId.of("UTC")));
    String formattedRouteDateTime = DTF_ISO_8601_LITE.format(
        routeDate.withZoneSameInstant(ZoneId.of("UTC")));

    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);
    String createRouteRequestJson = StandardTestUtils
        .replaceTokens(resolvedDataTable.get("createRouteRequest"),
            StandardTestUtils.createDefaultTokens());
    RouteRequest createRouteRequest = fromJson(createRouteRequestJson, RouteRequest.class);

    if (createRouteRequest.getDate() == null) {
      createRouteRequest.setDate(formattedRouteDate);
    }

    if (createRouteRequest.getDateTime() == null) {
      createRouteRequest.setDateTime(formattedRouteDateTime);
    }

    if (createRouteRequest.getComments() == null) {
      String comments = f(
          "This route is created for testing purpose only. Ignore this route. Created at %s by scenario \"%s\".",
          createdDate, scenarioName);
      createRouteRequest.setComments(comments);
    }

    // to cater for scenario that needs route of hub id same as destination hub of created parcel
    if (createRouteRequest.getHubId() == null) {
      createRouteRequest.setHubId(get(KEY_DESTINATION_HUB_ID));
    }
    return createRouteRequest;
  }

  @Given("API Route - set tags to route:")
  public void apiOperatorSetTagsOfTheNewCreatedRouteTo(Map<String, String> data) {
    Map<String, String> finalData = resolveKeyValues(data);
    doWithRetry(() -> {
      int[] tagIds = splitAndNormalize(finalData.get("tagIds")).stream()
          .mapToInt(Integer::parseInt).toArray();
      long routeId = Long.parseLong(finalData.get("routeId"));
      getRouteClient().setRouteTags(routeId, tagIds);
    }, "Set tags to route");
  }

  /**
   * Sample:<p>
   *
   * @param data <br><b>name:</b>
   *             AAA<br><b>description:</b> This tag AAA is for testing
   */
  @Given("API Route - create new route tag:")
  public void apiOperatorCreateRouteTag(Map<String, String> data) {
    final Map<String, String> dataTable = resolveKeyValues(data);
    final String json = toJsonSnakeCase(dataTable);
    final RouteTag request = fromJsonSnakeCase(json, RouteTag.class);
    doWithRetry(() -> {
      TagResponse response = getTagClient().create(request);
      response.getData().getTags().forEach(createTag -> {
        if (createTag.getName().equalsIgnoreCase(request.getName())) {
          putInList(KEY_CORE_LIST_OF_CREATED_ROUTE_TAGS, createTag);
        }
      });
    }, "create tag", 1000, 5);
  }

  /**
   * Sample:<p>
   * <p>
   * Given API Route - Operator add transactions to "{KEY_LIST_OF_CREATED_ROUTE_GROUPS[1].id}":
   * <p> | {KEY_LIST_OF_CREATED_ORDERS[1].transactions[1].id} |</p>
   * <p>| {KEY_LIST_OF_CREATED_ORDERS[1].transactions[2].id} |</p>
   */
  @When("API Route - Operator add transactions to {string}:")
  public void operatorAddTxnToRouteGroup(String routeGroupId, List<String> transactionIds) {
    final long id = Long.parseLong(resolveValue(routeGroupId));
    transactionIds = resolveValues(transactionIds);
    List<Long> ids = transactionIds.stream().map(Long::parseLong).collect(Collectors.toList());
    doWithRetry(
        () -> getRouteClient().addTransactionsToRouteGroup(id, ids),
        "add transactions route group", 1000, 5);
  }

  /**
   * Sample:<p>
   * <p>
   * Given API Route - Operator add reservations to "{KEY_LIST_OF_CREATED_ROUTE_GROUPS[1].id}":
   * <p> | {KEY_LIST_OF_CREATED_RESERVATIONS[1].id} |</p>
   * <p>| {KEY_LIST_OF_CREATED_RESERVATIONS[2].id} |</p>
   */
  @When("API Route - Operator add reservations to {string}:")
  public void operatorAddRsvnToRouteGroup(String routeGroupId, List<String> rsvnIds) {
    final long id = Long.parseLong(resolveValue(routeGroupId));
    rsvnIds = resolveValues(rsvnIds);
    List<Long> ids = rsvnIds.stream().map(Long::parseLong).collect(Collectors.toList());
    doWithRetry(
        () -> getRouteClient().addReservationsToRouteGroup(id, ids),
        "add reservations route group", 1000, 5);
  }

}
