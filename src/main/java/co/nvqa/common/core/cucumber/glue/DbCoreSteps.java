package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.OrderDao;
import co.nvqa.common.core.hibernate.OrderDetailsDao;
import co.nvqa.common.core.hibernate.ReservationsDao;
import co.nvqa.common.core.hibernate.RouteLogsDao;
import co.nvqa.common.core.hibernate.RouteMonitoringDataDao;
import co.nvqa.common.core.hibernate.ShipperPickupSearchDao;
import co.nvqa.common.core.hibernate.TransactionsDao;
import co.nvqa.common.core.hibernate.WaypointsDao;
import co.nvqa.common.core.model.order.Order.Data;
import co.nvqa.common.core.model.order.Order.PreviousAddressDetails;
import co.nvqa.common.core.model.persisted_class.core.OrderDetails;
import co.nvqa.common.core.model.persisted_class.core.Orders;
import co.nvqa.common.core.model.persisted_class.core.Reservations;
import co.nvqa.common.core.model.persisted_class.core.RouteLogs;
import co.nvqa.common.core.model.persisted_class.core.RouteMonitoringData;
import co.nvqa.common.core.model.persisted_class.core.ShipperPickupSearch;
import co.nvqa.common.core.model.persisted_class.core.Transactions;
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
  private TransactionsDao transactionsDao;

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
    final long orderId = Long.parseLong(expectedData.get("order_id"));
    final double expectedWeight = Double.parseDouble(source.get("weight"));
    if (Boolean.parseBoolean(expectedData.get("use_weight_range"))) {
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
    final long orderId = Long.parseLong(expectedData.get("order_id"));
    final double expectedWeight = Double.parseDouble(source.get("weight"));
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
    Map<String, String> resolvedData = resolveKeyValues(data);
    RouteLogs expected = new RouteLogs(resolvedData);

    retryIfAssertionErrorOccurred(() -> {
      RouteLogs actual = routeLogsDao.getRouteLogs(expected.getId());
      Assertions.assertThat(actual)
          .withFailMessage("Roure logs was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, f("verify route_logs records"), 10_000, 3);
  }

  @When("DB Core - verify waypoints record:")
  public void verifyWaypoints(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Waypoints expected = new Waypoints(resolvedData);

    retryIfAssertionErrorOccurred(() -> {
      Waypoints actual = waypointsDao.getWaypointsDetails(expected.getId());
      Assertions.assertThat(actual)
          .withFailMessage("waypoints record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
      if (resolvedData.containsKey("seqNo") && resolvedData.get("seqNo").equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getSeqNo())
            .as("seq_no is null")
            .isNull();
      }
      if (resolvedData.containsKey("routeId") && resolvedData.get("routeId")
          .equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getSeqNo())
            .as("route_id is null")
            .isNull();
      }
    }, "verify waypoints records", 10_000, 3);
  }

  @When("DB Core - verify shipper_pickup_search record:")
  public void verifyShipperPickupSearch(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    ShipperPickupSearch expected = new ShipperPickupSearch(resolvedData);

    retryIfAssertionErrorOccurred(() -> {
      ShipperPickupSearch actual = null;
      if (expected.getReservationId() != null) {
        actual = shipperPickupSearchDao.getShipperPickupSearchByReservationId(
            expected.getReservationId());
      }
      Assertions.assertThat(actual)
          .withFailMessage("shipper_pickup_search record was not found: " + data)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify shipper_pickup_search records", 10_000, 3);
  }

  @When("DB Core - verify route_monitoring_data record:")
  public void verifyRouteMonitoringData(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    RouteMonitoringData expected = new RouteMonitoringData(resolvedData);

    retryIfAssertionErrorOccurred(() -> {
      RouteMonitoringData actual = null;
      if (expected.getWaypointId() != null) {
        actual = routeMonitoringDataDao.getRouteMonitoringDataByWaypointId(
            expected.getWaypointId());
      }
      Assertions.assertThat(actual)
          .withFailMessage("route_monitoring_data record was not found: " + data)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify route_monitoring_data records", 10_000, 3);
  }

  @When("DB Core - verify route_monitoring_data is hard-deleted:")
  public void verifyRouteMonitoringData(List<String> data) {
    List<String> resolvedData = resolveValues(data);
    retryIfAssertionErrorOccurred(() -> {
      resolvedData.forEach(e -> {
        RouteMonitoringData actual = routeMonitoringDataDao
            .getRouteMonitoringDataByWaypointId(Long.parseLong(e));
        Assertions.assertThat(actual)
            .as("route_monitoring_data is hard-deleted").isNull();
      });
    }, "verify route_monitoring_data records", 10_000, 3);

  }

  @When("DB Core - operator get order details of order id {string}")
  public void getOrderDetailsByOrderId(String orderId) {
    Long resolvedOrderId = Long.parseLong(resolveValue(orderId));
    OrderDetails orderDetails = orderDetailsDao.getOrderDetailsByOrderId(resolvedOrderId);
    putInList(KEY_CORE_LIST_OF_ORDER_DETAILS, orderDetails);
  }

  @When("DB Core - operator verify orders.data.previousDeliveryDetails is updated correctly:")
  public void verifyOrdersDataUpdated(Map<String, String> source) {
    Long resolvedOrderId = Long.parseLong(resolveValue(source.get("orderId")));
    Map<String, String> resolvedMap = resolveKeyValues(source);
    retryIfAssertionErrorOccurred(() -> {
      String orderData = orderDao.getSingleOrderDetailsById(resolvedOrderId).getData();
      List<PreviousAddressDetails> previousAddressDetails = fromJsonCamelCase(orderData, Data.class)
          .getPreviousDeliveryDetails();
      PreviousAddressDetails actual = previousAddressDetails.get(previousAddressDetails.size() - 1);
      PreviousAddressDetails expected = new PreviousAddressDetails(resolvedMap);
      Assertions.assertThat(actual)
          .withFailMessage("previous address details not found")
          .isNotNull();
      expected.compareWithActual(actual, resolvedMap);
    }, "verify previousDeliveryDetails", 10_000, 3);
  }

  @When("DB Core - verify transactions record:")
  public void verifyTransaction(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Transactions expected = new Transactions(resolvedData);
    retryIfAssertionErrorOccurred(() -> {
      Transactions actual = transactionsDao.getSingleTransaction(expected.getId());
      Assertions.assertThat(actual)
          .withFailMessage("transactions record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
      if (data.containsKey("distributionPointId") && data.get("distributionPointId")
          .equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getDistributionPointId())
            .as("distributionPointId is null")
            .isNull();
      }
      if (data.containsKey("routeId") && data.get("routeId").equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getRouteId())
            .as("route_id is null")
            .isNull();
      }
    }, "verify transactions records", 10_000, 3);
  }

  @Given("DB Core - verify transactions after RTS:")
  public void dbOperatorVerifiesTxnAfterRts(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    final Long orderId = Long.parseLong(resolvedData.get("orderId"));
    final Long routeId = Long.parseLong(resolvedData.get("routeId"));
    retryIfAssertionErrorOccurred(() -> {
      List<Transactions> result = transactionsDao.getMultipleTransactions(orderId);
      Assertions.assertThat(result.size()).as("number of transactions")
          .isEqualTo(Integer.parseInt(data.get("number_of_txn")));
      Assertions.assertThat(result.get(0).getStatus()).as("pickup status").isEqualTo("Success");

      if (Integer.parseInt(data.get("number_of_txn")) == 2) {
        Assertions.assertThat(result.get(1).getStatus()).as("delivery status")
            .isEqualTo(data.get("delivery_status"));
        Assertions.assertThat(result.get(1).getRouteId() == 0).as("route id is null").isTrue();
        put(KEY_WAYPOINT_ID, result.get(1).getWaypointId());
      } else {
        Assertions.assertThat(result.get(1).getStatus()).as("old delivery status")
            .isEqualTo(data.get("old_delivery_status"));
        Long actualRouteId = result.get(1).getRouteId();
        Assertions.assertThat(actualRouteId).as("old route id").isEqualTo(routeId);
        Assertions.assertThat(result.get(2).getStatus()).as("new delivery status")
            .isEqualTo(data.get("new_delivery_status"));
        Assertions.assertThat(result.get(2).getType()).as("new delivery type")
            .isEqualTo(data.get("new_delivery_type"));
        if (result.get(1).getStatus().equalsIgnoreCase("Fail")) {
          Assertions.assertThat(result.get(2).getRouteId() == 0).as("new route id is null")
              .isTrue();
        } else {
          Assertions.assertThat(result.get(2).getRouteId()).as("old route id")
              .isEqualTo(routeId);
        }
      }
    }, "check transactions");
  }

  @When("DB Core - verify orders record:")
  public void verifyOrderRecords(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Orders expected = new Orders(resolvedData);
    retryIfAssertionErrorOccurred(() -> {
      Orders actual = orderDao.getSingleOrderDetailsById(expected.getId());
      Assertions.assertThat(actual)
          .withFailMessage("orders record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify orders records", 10_000, 3);
  }
}
