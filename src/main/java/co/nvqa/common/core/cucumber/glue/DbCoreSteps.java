package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.OrderDao;
import co.nvqa.common.core.hibernate.OrderDetailsDao;
import co.nvqa.common.core.hibernate.ReservationsDao;
import co.nvqa.common.core.hibernate.RouteLogsDao;
import co.nvqa.common.core.hibernate.RouteMonitoringDataDao;
import co.nvqa.common.core.hibernate.ShipperPickupSearchDao;
import co.nvqa.common.core.hibernate.WaypointsDao;
import co.nvqa.common.core.model.persisted_class.core.OrderDetails;
import co.nvqa.common.core.model.persisted_class.core.Reservations;
import co.nvqa.common.core.model.persisted_class.core.RouteLogs;
import co.nvqa.common.core.model.persisted_class.core.RouteMonitoringData;
import co.nvqa.common.core.model.persisted_class.core.ShipperPickupSearch;
import co.nvqa.common.core.model.persisted_class.core.Waypoints;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;

public class DbCoreSteps extends CoreStandardSteps {

  @Inject
  private OrderDao orderDao;

  @Inject
  private OrderDetailsDao orderDetailsDao;

  @Inject
  private RouteLogsDao routeLogsDao;

  @Inject
  private WaypointsDao waypointsDao;
  @Inject
  private ShipperPickupSearchDao shipperPickupSearchDao;
  @Inject
  private RouteMonitoringDataDao routeMonitoringDataDao;

  @Inject
  private ReservationsDao reservationDao;

  @Override
  public void init() {
  }

  @Given("DB Core - verify order weight updated to highest weight within range")
  public void dbOperatorVerifiesHighestOrderWeight(Map<String, String> source) {
    Map<String, String> expectedData = resolveKeyValues(source);
    final long orderId = Long.valueOf(expectedData.get("order_id"));
    final double expectedWeight = Double.valueOf(source.get("weight"));
    if (Boolean.valueOf(expectedData.get("use_weight_range"))) {
      dbOperatorVerifiesOrderWeightRangeUpdated(expectedData);
    } else {
      retryIfAssertionErrorOccurred(() -> {
        double actualWeight = orderDao.getOrderWeight(orderId);
        Assertions.assertThat(actualWeight).as("orders.weight equals highest weight")
            .isEqualTo(expectedWeight);
        put(KEY_SAVED_ORDER_WEIGHT, actualWeight);
      }, f("Get orders.weight of id %s ", orderId), 10_000, 3);
    }
  }

  @Given("DB Core - verify order weight range updated correctly")
  public void dbOperatorVerifiesOrderWeightRangeUpdated(Map<String, String> source) {

    Map<String, String> expectedData = resolveKeyValues(source);
    final long orderId = Long.valueOf(expectedData.get("order_id"));
    final double expectedWeight = Double.valueOf(source.get("weight"));
    final Double higherBound = expectedWeight;
    final Double lowerBound = expectedWeight - 0.5;

    retryIfAssertionErrorOccurred(() -> {
      double actualWeight = orderDao.getOrderWeight(orderId);
      Assertions.assertThat(actualWeight >= lowerBound)
          .as("Order weight should be greater than " + expectedWeight + " - 0.5").isTrue();
      Assertions.assertThat(actualWeight <= higherBound)
          .as("Order weight should be lover than " + expectedWeight + " - 0").isTrue();
      put(KEY_SAVED_ORDER_WEIGHT, actualWeight);
    }, f("get orders weight of order id %s", orderId), 10_000, 3);
  }

  @And("DB Core - get waypoint Id from reservation id {string}")
  public void coreGetWaypointFromReservationId(String reservationId) {
    Long resolvedReservationIdKey = Long.parseLong(resolveValue(reservationId));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      List<Reservations> results = reservationDao.getReservationsDetailsByReservationId(
          resolvedReservationIdKey);
      put(KEY_WAYPOINT_ID, results.get(0).getWaypointId());
    }, "Validating verified WayPoint Id value is as expected", 2000, 3);
  }

  @Then("DB Core - verifies that zone type is equal to {string} and zone id is not null for waypointId {string}")
  public void dbCoreVerifiesThatZoneIdEqualTo(String expectedZoneType, String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      Waypoints result = waypointsDao.getWaypointsDetails(resolvedWayPointIdKey);
      Assertions.assertThat(result.getZoneType())
          .as("Assertion for Zone Type column value is as expected").isEqualTo(expectedZoneType);
      Assertions.assertThat(result.getRoutingZoneId())
          .as("Assertion for Zone Id column value is as expected").isNotNull();
    }, "Validating verified Zone Type value is as expected", 2000, 3);
  }

  @Then("DB Core - verifies that zone type is equal to {string} and zone id is null for waypointId {string}")
  public void dbCoreVerifiesThatZoneIdEqualToNull(String expectedZoneType, String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      Waypoints result = waypointsDao.getWaypointsDetails(resolvedWayPointIdKey);
      Assertions.assertThat(result.getZoneType())
          .as("Assertion for Zone Type column value is as expected").isEqualTo(expectedZoneType);
      Assertions.assertThat(result.getRoutingZoneId())
          .as("Assertion for Zone Id column value is as expected").isNull();
    }, "Validating verified Zone Type value is as expected", 2000, 3);
  }

  @When("DB Core - verify route_logs record:")
  public void verifyRouteLogs(Map<String, String> data) {
    data = resolveKeyValues(data);
    RouteLogs expected = new RouteLogs(data);
    RouteLogs actual = routeLogsDao.getRouteLogs(expected.getId());
    Assertions.assertThat(actual)
        .withFailMessage("Roure logs was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
  }

  @When("DB Core - verify waypoints record:")
  public void verifyWaypoints(Map<String, String> data) {
    data = resolveKeyValues(data);
    Waypoints expected = new Waypoints(data);
    Waypoints actual = waypointsDao.getWaypointsDetails(expected.getId());
    Assertions.assertThat(actual)
        .withFailMessage("waypoints record was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
  }

  @When("DB Core - verify shipper_pickup_search record:")
  public void verifyShipperPickupSearch(Map<String, String> data) {
    data = resolveKeyValues(data);
    ShipperPickupSearch expected = new ShipperPickupSearch(data);
    ShipperPickupSearch actual = null;
    if (expected.getReservationId() != null) {
      actual = shipperPickupSearchDao.getShipperPickupSearchByReservationId(
          expected.getReservationId());
    }
    Assertions.assertThat(actual)
        .withFailMessage("shipper_pickup_search record was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
  }

  @When("DB Core - verify route_monitoring_data record:")
  public void verifyRouteMonitoringData(Map<String, String> data) {
    data = resolveKeyValues(data);
    RouteMonitoringData expected = new RouteMonitoringData(data);
    RouteMonitoringData actual = null;
    if (expected.getWaypointId() != null) {
      actual = routeMonitoringDataDao.getRouteMonitoringDataByWaypointId(
          expected.getWaypointId());
    }
    Assertions.assertThat(actual)
        .withFailMessage("route_monitoring_data record was not found: " + data)
        .isNotNull();
    expected.compareWithActual(actual, data);
  }

  @When("DB Core - operator get order details of order id {string}")
  public void getOrderDetailsByOrderId(String orderId) {
    Long resolvedOrderId = Long.parseLong(resolveValue(orderId));
    OrderDetails orderDetails = orderDetailsDao.getOrderDetailsByOrderId(resolvedOrderId);
    putInList(KEY_CORE_LIST_OF_ORDER_DETAILS, orderDetails);
  }
}
