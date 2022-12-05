package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.route.AddParcelToRouteRequest;
import co.nvqa.common.core.model.route.RouteRequest;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.core.utils.CoreTestUtils;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.common.utils.StandardTestUtils;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.java.en.Given;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ApiRouteSteps extends CoreStandardSteps {

  private RouteClient routeClient;

  @Override
  public void init() {

  }

  /**
   * Sample:
   * <p>
   * Given API Operator create new route using data below:
   * | createRouteRequest | { "zoneId":{zone-id}, "hubId":{hub-id}, "vehicleId":{vehicle-id}, "driverId":{ninja-driver-id} } |
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @Given("API Core - Operator create new route using data below:")
  public void apiOperatorCreateNewRouteUsingDataBelow(Map<String, String> dataTableAsMap) {
    String scenarioName = getScenarioManager().getCurrentScenario().getName();

    LocalDateTime routeDateForToday = CoreTestUtils.getRouteDateForToday();

    DateTimeFormatter utcDtf = DTF_NORMAL_DATETIME.withZone(ZoneId.of("UTC"));
    String createdDate = DTF_CREATED_DATE.format(ZonedDateTime.now());
    String formattedRouteDate = utcDtf.format(routeDateForToday);
    String formattedRouteDateTime = utcDtf.format(routeDateForToday);

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
  }

  /**
   * Sample:<p>
   * <p>
   * When API Operator add parcel to the route using data below:<p>
   * | orderId | 111111 |<p>
   * | addParcelToRouteRequest | {"tracking_id":"NVQASG","route_id":95139463,"type":"DELIVERY"} |<p>
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

  private RouteClient getRouteClient() {
    if (routeClient == null) {
      routeClient = new RouteClient(StandardTestConstants.API_BASE_URL,
          TokenUtils.getOperatorAuthToken());
    }

    return routeClient;
  }
}
