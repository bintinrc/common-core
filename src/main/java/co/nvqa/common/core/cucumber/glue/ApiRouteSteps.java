package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.route.AddParcelToRouteRequest;
import co.nvqa.common.core.model.route.AddPickupJobToRouteRequest;
import co.nvqa.common.core.model.route.RouteRequest;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.core.model.waypoint.Waypoint;
import co.nvqa.common.core.utils.CoreTestUtils;
import co.nvqa.common.utils.StandardTestUtils;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.assertj.core.api.Assertions;

@ScenarioScoped
public class ApiRouteSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private RouteClient routeClient;

  @Override
  public void init() {

  }

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

    final RouteResponse createRouteResponse = getRouteClient().createRoute(createRouteRequest);
    putInList(KEY_LIST_OF_CREATED_ROUTES, createRouteResponse);
    putInList(KEY_LIST_OF_CREATED_ROUTE_ID, createRouteResponse.getId());
    put(KEY_CREATED_ROUTE_ID, createRouteResponse.getId());
  }

  @Given("API Core - Operator create new route from zonal routing using data below:")
  public void apiOperatorCreateNewRouteFromZonalRoutingUsingDataBelow(
      Map<String, String> dataTableAsMap) {
    apiOperatorCreateNewRouteUsingDataBelow(dataTableAsMap);
  }

  /**
   * Sample:<p>
   * <p>
   * When API Operator add parcel to the route using data below:<p> | orderId | 111111 |<p> |
   * addParcelToRouteRequest | {"tracking_id":"NVQASG","route_id":95139463,"type":"DELIVERY"} |<p>
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
    retryIfAssertionErrorOccurred(
        () -> getRouteClient().addParcelToRoute(orderId, addParcelToRouteRequest),
        "add parcel to route");
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
    retryIfAssertionErrorOccurred(
        () -> getRouteClient().addPickupJobToRoute(jobId, addPickupJobToRouteRequest),
        "add pickup job to route");
  }

  @Given("API Core - Operator update routed waypoint to pending")
  public void operatorUpdateWaypointToPending(Map<String, String> dataTableAsMap) {
    final String json = toJsonCamelCase(resolveKeyValues(dataTableAsMap));
    final Waypoint waypoint = fromJsonCamelCase(json, Waypoint.class);
    final List<Waypoint> request = Collections.singletonList(waypoint);
    retryIfAssertionErrorOccurred(
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
    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getRouteClient().archiveRoutes(ids),
        "Archive route", 1000, 5);
  }

  @When("API Core - Operator unarchives routes below:")
  public void operatorUnarchivesRoutes(List<String> routeIds) {
    routeIds = resolveValues(routeIds);
    List<Long> ids = routeIds.stream().map(Long::parseLong).collect(Collectors.toList());
    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getRouteClient().unarchiveRoutes(ids),
        "Unarchive route", 1000, 5);
  }

  @When("API Core - Operator unarchives invalid route with data below:")
  public void operatorUnArchiveRouteInvalidState(Map<String, String> mapOfData) {
    Map<String, String> expectedData = resolveKeyValues(mapOfData);
    final long routeId = Long.valueOf(expectedData.get("routeId"));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(
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
    retryIfAssertionErrorOrRuntimeExceptionOccurred(
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
    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getRouteClient().pullFromRoute(orderId, type),
        "Operator pull order from route");
  }

  /**
   * Sample:<p>
   * <p>
   * When API Operator add reservation pick-ups to the route using data below:<p> | reservationId |
   * 111111 |<p> | routeId       | 222222 |<p>
   * <p>
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator add reservation pick-ups to the route using data below:")
  public void apiOperatorAddReservationPickUpsToTheRoute(Map<String, String> dataTableAsMap) {
    Map<String, String> resolvedDataTable = resolveKeyValues(dataTableAsMap);

    final long reservationResultId = Long.parseLong(resolvedDataTable.get("reservationId"));
    final long routeId = Long.parseLong(resolvedDataTable.get("routeId"));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getRouteClient().addReservationToRoute(routeId, reservationResultId),
        "add reservation to route ");
  }
}
