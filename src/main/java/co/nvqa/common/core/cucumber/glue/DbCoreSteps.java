package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.OrderDetailsDao;
import co.nvqa.common.core.hibernate.OrderJaroScoresV2Dao;
import co.nvqa.common.core.hibernate.ReservationsDao;
import co.nvqa.common.core.hibernate.RouteLogsDao;
import co.nvqa.common.core.hibernate.RouteMonitoringDataDao;
import co.nvqa.common.core.hibernate.ShipperPickupSearchDao;
import co.nvqa.common.core.hibernate.TransactionsDao;
import co.nvqa.common.core.hibernate.WaypointsDao;
import co.nvqa.common.core.model.persisted_class.core.OrderDetails;
import co.nvqa.common.core.model.persisted_class.core.OrderJaroScoresV2;
import co.nvqa.common.core.model.persisted_class.core.Reservations;
import co.nvqa.common.core.model.persisted_class.core.RouteLogs;
import co.nvqa.common.core.model.persisted_class.core.RouteMonitoringData;
import co.nvqa.common.core.model.persisted_class.core.ShipperPickupSearch;
import co.nvqa.common.core.model.persisted_class.core.Transactions;
import co.nvqa.common.core.model.persisted_class.core.Waypoints;
import co.nvqa.common.model.DataEntity;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;

public class DbCoreSteps extends CoreStandardSteps {

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
  @Inject
  private OrderJaroScoresV2Dao orderJaroScoresV2Dao;

  @Override
  public void init() {
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

  @Then("DB Core - operator get waypoints details for {string}")
  public void dbCoreGetWaypointDetails(String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      Waypoints result = waypointsDao.getWaypointsDetails(resolvedWayPointIdKey);
      put(KEY_CORE_WAYPOINT_DETAILS, result);
    }, "get core waypoint details", 2000, 3);
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
        Assertions.assertThat(result.get(1).getRouteId()).as("route id is null").isNull();
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
          Assertions.assertThat(result.get(2).getRouteId()).as("new route id is null")
              .isNull();
        } else {
          Assertions.assertThat(result.get(2).getRouteId()).as("old route id")
              .isEqualTo(routeId);
        }
      }
    }, "check transactions");
  }

  @Given("DB Core - verify number of transactions is correct after new transactions created")
  public void dbOperatorVerifiesCreatedTransactions(Map<String, String> mapOfData) {

    retryIfAssertionErrorOccurred(() -> {
      Map<String, String> expectedData = resolveKeyValues(mapOfData);
      List<Transactions> result = transactionsDao
          .getMultipleTransactions(Long.parseLong((expectedData.get("order_id"))));
      if (mapOfData.containsKey("number_of_transactions")) {
        Assertions.assertThat(result.size()).as("number of transactions")
            .isEqualTo(Integer.parseInt(expectedData.get("number_of_transactions")));
      }
      if (mapOfData.containsKey("number_of_pickup_txn")) {
        List<Transactions> pickupTxns = result.stream()
            .filter(e -> e.getType().equalsIgnoreCase("PP")).collect(
                Collectors.toList());
        Assertions.assertThat(pickupTxns.size()).as("number of pickup transactions")
            .isEqualTo(Integer.parseInt(expectedData.get("number_of_pickup_txn")));
        //to check newly created pickup transaction address details
        if (mapOfData.containsKey("pickup_address")) {
          Transactions txn = pickupTxns.stream()
              .filter(e -> e.getStatus().equalsIgnoreCase("PENDING")).findAny().orElseThrow(
                  () -> new NvTestRuntimeException("new transaction status is not pending"));
          String actualPickupAddress = f("%s %s %s %s", txn.getAddress1(), txn.getAddress2(),
              txn.getPostcode(), txn.getCountry());
          Assertions.assertThat(actualPickupAddress).as("pickup transaction address details")
              .isEqualToIgnoringCase(expectedData.get("pickup_address"));
        }
      }

      if (mapOfData.containsKey("number_of_delivery_txn")) {
        List<Transactions> deliveryTxns = result.stream()
            .filter(e -> e.getType().equalsIgnoreCase("DD")).collect(
                Collectors.toList());
        Assertions.assertThat(deliveryTxns.size()).as("number of delivery transactions")
            .isEqualTo(Integer.parseInt(expectedData.get("number_of_delivery_txn")));
        //to check newly created delivery transaction address details
        if (mapOfData.containsKey("delivery_address")) {
          Transactions txn = deliveryTxns.stream()
              .filter(e -> e.getStatus().equalsIgnoreCase("PENDING")).findAny().orElseThrow(
                  () -> new NvTestRuntimeException("new transaction status is not pending"));
          String actualDeliveryAddress = f("%s %s %s %s", txn.getAddress1(), txn.getAddress2(),
              txn.getPostcode(), txn.getCountry());
          Assertions.assertThat(actualDeliveryAddress).as("delivery transaction address details")
              .isEqualToIgnoringCase(expectedData.get("delivery_address"));
        }
      }
    }, "check transactions");
  }

  @When("DB Core - verify order_jaro_scores_v2 record:")
  public void dbOperatorVerifyJaroScores(List<Map<String, String>> data) {
    List<Map<String, String>> resolvedMap = resolveListOfMaps(data);
    List<OrderJaroScoresV2> expected = new ArrayList<>();
    resolvedMap.forEach(e -> {
      OrderJaroScoresV2 jaroScore = new OrderJaroScoresV2(e);
      expected.add(jaroScore);
    });
    retryIfAssertionErrorOccurred(() -> {
      OrderJaroScoresV2 actualJaroScores = expected.get(0);
      List<OrderJaroScoresV2> actual = orderJaroScoresV2Dao
          .getMultipleOjs(actualJaroScores.getWaypointId());
      Assertions.assertThat(actual)
          .as("List of Jaro Scores for waypoint id" + actualJaroScores.getWaypointId())
          .isNotEmpty();
      actual
          .forEach(o -> DataEntity.assertListContains(expected, o, "ojs list"));
    }, "verify order_jaro_scores_v2 records", 10_000, 3);
  }
}
