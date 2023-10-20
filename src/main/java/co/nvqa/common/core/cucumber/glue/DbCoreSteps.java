package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.CodCollectionDao;
import co.nvqa.common.core.hibernate.CodInboundsDao;
import co.nvqa.common.core.hibernate.InboundScansDao;
import co.nvqa.common.core.hibernate.OrderDao;
import co.nvqa.common.core.hibernate.OrderDeliveryDetailsDao;
import co.nvqa.common.core.hibernate.OrderDeliveryVerificationsDao;
import co.nvqa.common.core.hibernate.OrderDetailsDao;
import co.nvqa.common.core.hibernate.OrderJaroScoresV2Dao;
import co.nvqa.common.core.hibernate.OrderTagsDao;
import co.nvqa.common.core.hibernate.OrderTagsSearchDao;
import co.nvqa.common.core.hibernate.OutboundScansDao;
import co.nvqa.common.core.hibernate.ReservationsDao;
import co.nvqa.common.core.hibernate.RouteDbDao;
import co.nvqa.common.core.hibernate.RouteMonitoringDataDao;
import co.nvqa.common.core.hibernate.RouteWaypointDao;
import co.nvqa.common.core.hibernate.ShipperPickupSearchDao;
import co.nvqa.common.core.hibernate.TransactionsDao;
import co.nvqa.common.core.hibernate.WarehouseSweepsDao;
import co.nvqa.common.core.hibernate.WaypointsDao;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.Order.Transaction;
import co.nvqa.common.core.model.persisted_class.core.CodCollections;
import co.nvqa.common.core.model.persisted_class.core.CodInbounds;
import co.nvqa.common.core.model.persisted_class.core.InboundScans;
import co.nvqa.common.core.model.persisted_class.core.OrderDeliveryVerifications;
import co.nvqa.common.core.model.persisted_class.core.OrderDetails;
import co.nvqa.common.core.model.persisted_class.core.OrderJaroScoresV2;
import co.nvqa.common.core.model.persisted_class.core.OrderTags;
import co.nvqa.common.core.model.persisted_class.core.OrderTagsSearch;
import co.nvqa.common.core.model.persisted_class.core.Orders;
import co.nvqa.common.core.model.persisted_class.core.OutboundScans;
import co.nvqa.common.core.model.persisted_class.core.Reservations;
import co.nvqa.common.core.model.persisted_class.core.RouteMonitoringData;
import co.nvqa.common.core.model.persisted_class.core.RouteWaypoint;
import co.nvqa.common.core.model.persisted_class.core.ShipperPickupSearch;
import co.nvqa.common.core.model.persisted_class.core.Transactions;
import co.nvqa.common.core.model.persisted_class.core.WarehouseSweeps;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

public class DbCoreSteps extends CoreStandardSteps {

  @Inject
  private OrderDetailsDao orderDetailsDao;
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
  @Inject
  private WarehouseSweepsDao warehouseSweepsDao;
  @Inject
  private OutboundScansDao outboundScansDao;
  @Inject
  private CodInboundsDao codInboundsDao;
  @Inject
  private OrderTagsDao orderTagsDao;
  @Inject
  private OrderTagsSearchDao orderTagsSearchDao;
  @Inject
  private RouteWaypointDao routeWaypointDao;
  @Inject
  private RouteDbDao routeDbDao;
  @Inject
  private CodCollectionDao codCollectionDao;
  @Inject
  private InboundScansDao inboundScansDao;
  @Inject
  private OrderDeliveryVerificationsDao orderDeliveryVerificationsDao;
  @Inject
  private OrderDeliveryDetailsDao orderDeliveryDetailsDao;

  @Inject
  private OrderDao orderDao;


  @And("DB Core - get Reservation data from reservation id {string}")
  public void coreGetReservationDataFromReservationId(String reservationId) {
    Long resolvedReservationIdKey = Long.parseLong(resolveValue(reservationId));
    doWithRetry(() -> {
      Reservations result = reservationDao.getReservationsDetailsByReservationId(
          resolvedReservationIdKey);
      putInList(KEY_CORE_LIST_OF_RESERVATIONS_DB, result);
    }, "Fetch ReservationData", 2000, 3);
  }

  @And("DB Core - get waypoint Id from reservation id {string}")
  public void coreGetWaypointFromReservationId(String reservationId) {
    Long resolvedReservationIdKey = Long.parseLong(resolveValue(reservationId));
    doWithRetry(() -> {
      Reservations result = reservationDao.getReservationsDetailsByReservationId(
          resolvedReservationIdKey);
      put(KEY_WAYPOINT_ID, result.getWaypointId());
    }, "Validating verified WayPoint Id value is as expected", 2000, 3);
  }

  @Then("DB Core - verifies that zone type is equal to {string} and zone id is not null for waypointId {string}")
  public void dbCoreVerifiesThatZoneIdEqualTo(String expectedZoneType, String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    doWithRetry(() -> {
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
    doWithRetry(() -> {
      Waypoints result = waypointsDao.getWaypointsDetails(resolvedWayPointIdKey);
      Assertions.assertThat(result.getZoneType())
          .as("Assertion for Zone Type column value is as expected").isEqualTo(expectedZoneType);
      if (result.getRoutingZoneId() == null) {
        Assertions.assertThat(result.getRoutingZoneId())
            .as("Assertion for Zone Id column value is null as expected").isNull();
      } else {
        Assertions.assertThat(result.getRoutingZoneId())
            .as("Assertion for Zone Id column value is zero as expected").isZero();
      }
    }, "Validating verified Zone Type value is as expected", 2000, 3);
  }

  @Then("DB Core - operator get waypoints details for {string}")
  public void dbCoreGetWaypointDetails(String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    doWithRetry(() -> {
      Waypoints result = waypointsDao.getWaypointsDetails(resolvedWayPointIdKey);
      put(KEY_CORE_WAYPOINT_DETAILS, result);
    }, "get core waypoint details", 2000, 3);
  }

  @Then("DB Core - verifies that latitude is equal to {string} and longitude is equal to {string} and for waypointId {string}")
  public void dbCoreVerifiesLatLong(String expectedLatitude, String expectedLongitude,
      String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    doWithRetry(() -> {
      Waypoints result = waypointsDao.getWaypointsDetails(resolvedWayPointIdKey);
      String[] formattedValues = formatLatLongValues(result.getLatitude(), result.getLongitude());
      Assertions.assertThat(formattedValues[0])
          .as("Assertion for lat column value is as expected").isEqualTo(expectedLatitude);
      Assertions.assertThat(formattedValues[1])
          .as("Assertion for lat column value is as expected").isEqualTo(expectedLongitude);
    }, "Validating verified lat long values are as expected");
  }

  private String[] formatLatLongValues(Double latitude, Double longitude) {
    String lat = latitude.toString();
    String formattedLatitude = lat.substring(0, 6);
    String lon = longitude.toString();
    String formattedLongitude = lon.substring(0, 6);
    return new String[]{formattedLatitude, formattedLongitude};
  }

  @When("DB Core - verify waypoints record:")
  public void verifyWaypoints(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Waypoints expected = new Waypoints(resolvedData);

    Assertions.assertThat(expected.getId())
        .as("waypoint id should not be null")
        .isNotNull();

    doWithRetry(() -> {
      Waypoints actual = waypointsDao.getWaypointsDetails(expected.getId());
      Assertions.assertThat(actual)
          .as("waypoints record was not found: " + resolvedData).isNotNull();
      expected.compareWithActual(actual, resolvedData);

      if (resolvedData.containsKey("seqNo")) {
        if (resolvedData.get("seqNo").equalsIgnoreCase("null")) {
          Assertions.assertThat(actual.getSeqNo())
              .as("seq_no is null")
              .isNull();
        } else {
          Assertions.assertThat(actual.getSeqNo())
              .as("seq_no is not null")
              .isNotNull();
        }
      }
      if (resolvedData.containsKey("routeId") && resolvedData.get("routeId")
          .equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getSeqNo()).as("route_id is null").isNull();
      }
    }, "verify waypoints records", 10_000, 3);
  }

  @When("DB Core - verify shipper_pickup_search record:")
  public void verifyShipperPickupSearch(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    ShipperPickupSearch expected = new ShipperPickupSearch(resolvedData);

    doWithRetry(() -> {
      ShipperPickupSearch actual = null;
      if (expected.getReservationId() != null) {
        actual = shipperPickupSearchDao.getShipperPickupSearchByReservationId(
            expected.getReservationId());
      }
      Assertions.assertThat(actual)
          .as("shipper_pickup_search record was not found: " + data).isNotNull();
      expected.compareWithActual(actual, resolvedData);
      if (resolvedData.containsKey("serviceTime")) {
        Assertions.assertThat(actual.getServiceEndTime())
            .as("shipper_pickup_search serviceEndTime is not null").isNotNull();
      }
      if (resolvedData.containsKey("failureReason")) {
        Assertions
            .assertThat(StringUtils
                .containsIgnoreCase(actual.getComments(), (resolvedData.get("failureReason"))))
            .as("shipper_pickup_search failureReason is correct").isTrue();
      }
    }, "verify shipper_pickup_search records", 10_000, 3);
  }

  @When("DB Core - verify route_monitoring_data record:")
  public void verifyRouteMonitoringData(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    RouteMonitoringData expected = new RouteMonitoringData(resolvedData);

    doWithRetry(() -> {
      RouteMonitoringData actual = routeMonitoringDataDao.getRouteMonitoringDataByWaypointId(
          expected.getWaypointId());
      Assertions.assertThat(actual)
          .as("route_monitoring_data record was not found: " + data).isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify route_monitoring_data records", 10_000, 3);
  }

  @When("DB Core - verify route_monitoring_data is hard-deleted:")
  public void verifyRouteMonitoringData(List<String> data) {
    List<String> resolvedData = resolveValues(data);
    doWithRetry(() ->
            resolvedData.forEach(e -> {
              RouteMonitoringData actual = routeMonitoringDataDao.getRouteMonitoringDataByWaypointId(
                  Long.parseLong(e));
              Assertions.assertThat(actual).as("route_monitoring_data is hard-deleted").isNull();
            })
        , "verify route_monitoring_data records", 10_000, 3);
  }

  @When("DB Core - operator get order details of order id {string}")
  public void getOrderDetailsByOrderId(String orderId) {
    Long resolvedOrderId = Long.parseLong(resolveValue(orderId));
    doWithRetry(
        () -> {
          OrderDetails orderDetails = orderDetailsDao.getOrderDetailsByOrderId(resolvedOrderId);
          putInList(KEY_CORE_LIST_OF_ORDER_DETAILS, orderDetails);
        }, "get order details");
  }

  @When("DB Core - operator get transaction records with:")
  public void getOrderDetailsByOrderId(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    doWithRetry(
        () -> {
          List<Transactions> result = transactionsDao.findTransactionByOrderIdAndType(
              Long.parseLong((resolvedData.get("order_id"))), resolvedData.get("type"));
          put(KEY_CORE_LIST_OF_TRANSACTIONS, result);
        }, "get transaction records");
  }

  @When("DB Core - verify transactions record:")
  public void verifyTransaction(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    Transactions expected = new Transactions(resolvedData);
    doWithRetry(() -> {
      Transactions actual = transactionsDao.getSingleTransaction(expected.getId());
      Assertions.assertThat(actual)
          .as("transactions record was not found: " + resolvedData).isNotNull();
      expected.compareWithActual(actual, resolvedData);
      if (data.containsKey("distributionPointId") && data.get("distributionPointId")
          .equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getDistributionPointId()).as("distributionPointId is null")
            .isNull();
      }
      if (data.containsKey("routeId") && data.get("routeId").equalsIgnoreCase("null")) {
        Assertions.assertThat(actual.getRouteId()).as("route_id is null").isNull();
      }
    }, "verify transactions records", 10_000, 3);
  }

  @Given("DB Core - verify transactions after RTS:")
  public void dbOperatorVerifiesTxnAfterRts(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    final Long orderId = Long.parseLong(resolvedData.get("orderId"));
    final Long routeId = Long.parseLong(resolvedData.get("routeId"));
    doWithRetry(() -> {
      List<Transactions> result = transactionsDao.getMultipleTransactionsByOrderId(orderId);
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
          Assertions.assertThat(result.get(2).getRouteId()).as("new route id is null").isNull();
        } else {
          Assertions.assertThat(result.get(2).getRouteId()).as("old route id").isEqualTo(routeId);
        }
      }
    }, "check transactions");
  }

  @Given("DB Core - verify number of transactions is correct after new transactions created")
  public void dbOperatorVerifiesCreatedTransactions(Map<String, String> mapOfData) {

    doWithRetry(() -> {
      Map<String, String> expectedData = resolveKeyValues(mapOfData);
      List<Transactions> result = transactionsDao.getMultipleTransactionsByOrderId(
          Long.parseLong((expectedData.get("order_id"))));
      put(KEY_CORE_LIST_OF_TRANSACTIONS, result);
      if (mapOfData.containsKey("number_of_transactions")) {
        Assertions.assertThat(result.size()).as("number of transactions")
            .isEqualTo(Integer.parseInt(expectedData.get("number_of_transactions")));
      }
      if (mapOfData.containsKey("number_of_pickup_txn")) {
        List<Transactions> pickupTxns = result.stream()
            .filter(e -> e.getType().equalsIgnoreCase("PP")).collect(Collectors.toList());
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
            .filter(e -> e.getType().equalsIgnoreCase("DD")).collect(Collectors.toList());
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

  @When("DB Core - verify multiple order_jaro_scores_v2 record:")
  public void dbOperatorVerifyMultipleJaroScores(List<Map<String, String>> data) {
    List<Map<String, String>> resolvedMap = resolveListOfMaps(data);
    List<OrderJaroScoresV2> expected = new ArrayList<>();
    resolvedMap.forEach(e -> {
      OrderJaroScoresV2 jaroScore = new OrderJaroScoresV2(e);
      expected.add(jaroScore);
    });
    doWithRetry(() -> {
      OrderJaroScoresV2 actualJaroScores = expected.get(0);
      List<OrderJaroScoresV2> actual = orderJaroScoresV2Dao.getMultipleOjs(
          actualJaroScores.getWaypointId());
      Assertions.assertThat(actual)
          .as("List of Jaro Scores for waypoint id" + actualJaroScores.getWaypointId())
          .isNotEmpty();
      actual.forEach(o -> DataEntity.assertListContains(expected, o, "ojs list"));
    }, "verify order_jaro_scores_v2 records", 10_000, 3);
  }

  @When("DB Core - verify number of records in order_jaro_scores_v2:")
  public void verifyOjsNumberOfRecords(Map<String, String> data) {
    Map<String, String> resolvedData = resolveKeyValues(data);
    final long waypointId = Long.parseLong(resolvedData.get("waypointId"));
    final long numberOfRecords = Integer.parseInt(resolvedData.get("number"));
    doWithRetry(() -> {
      List<OrderJaroScoresV2> actual = orderJaroScoresV2Dao.getMultipleOjs(waypointId);
      Assertions.assertThat(actual.size())
          .as(f("Expected %s records in order_jaro_scores_v2 table", numberOfRecords))
          .isEqualTo(numberOfRecords);
      put(KEY_CORE_LIST_OF_CREATED_OJS, actual);
    }, "verify records in order_jaro_scores_v2", 10_000, 3);
  }

  @When("DB Core - verify order_jaro_scores_v2 record:")
  public void dbOperatorVerifySingleJaroScores(Map<String, String> data) {
    final Map<String, String> resolvedData = resolveKeyValues(data);
    OrderJaroScoresV2 expected = new OrderJaroScoresV2(resolvedData);
    doWithRetry(() -> {
      OrderJaroScoresV2 actual = orderJaroScoresV2Dao.getSingleOjs(expected.getWaypointId(),
          expected.getArchived());
      Assertions.assertThat(actual)
          .as("order_jaro_scores_v2 record was not found: " + resolvedData)
          .isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "verify order_jaro_scores_v2 records", 10_000, 3);
  }

  @When("DB Core - verify warehouse_sweeps record:")
  public void verifyWarehouseSweeps(Map<String, String> dataTableRaw) {
    Map<String, String> dataTable = resolveKeyValues(dataTableRaw);
    String trackingId = dataTable.get("trackingId");
    Long hubId = Long.parseLong(dataTable.get("hubId"));
    Long orderId = Long.parseLong(dataTable.get("orderId"));

    doWithRetry(() -> {
      List<WarehouseSweeps> warehouseSweepRecords = warehouseSweepsDao
          .findWarehouseSweepRecord(trackingId);
      Assertions.assertThat(warehouseSweepRecords.size())
          .as("Expected 1 record in warehouse_sweeps table").isEqualTo(1);

      WarehouseSweeps theRecord = warehouseSweepRecords.get(0);

      Assertions.assertThat(theRecord.getScan()).as("Expected scan in warehouse_sweeps table")
          .isEqualTo(trackingId);
      Assertions.assertThat(theRecord.getHubId()).as("Expected hub_id in warehouse_sweeps table")
          .isEqualTo(hubId);
      Assertions.assertThat(theRecord.getOrderId())
          .as("Expected order_id in warehouse_sweeps table").isEqualTo(orderId);

      putInList(KEY_CORE_WAREHOUSE_SWEEPS, theRecord);

    }, "Verify record in warehouse_sweeps table", 2000, 10);
  }

  @When("DB Core - verify outbound_scans record:")
  public void verifyOutboundScans(Map<String, String> dataTableRaw) {
    Map<String, String> resolvedData = resolveKeyValues(dataTableRaw);
    OutboundScans expected = new OutboundScans(resolvedData);
    doWithRetry(() -> {
      OutboundScans actual = null;
      if (expected.getOrderId() != null) {
        actual = outboundScansDao.getOutboundScansByOrderId(expected.getOrderId());
      }
      Assertions.assertThat(actual)
          .as("outbound_scans record was not found: " + dataTableRaw).isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "Verify record in outbound_scans table", 2000, 10);
  }

  @Then("DB Core - verify cod_inbounds record:")
  public void verifyCodInboundsData(Map<String, String> dataTableRaw) {
    Map<String, String> resolvedData = resolveKeyValues(dataTableRaw);
    CodInbounds expected = new CodInbounds(resolvedData);
    doWithRetry(() -> {
      CodInbounds actual = null;
      if (expected.getRouteId() != null) {
        actual = codInboundsDao.getCodInboundsByRouteId(expected.getRouteId());
      }
      Assertions.assertThat(actual)
          .as("cod_inbounds record was not found: " + dataTableRaw).isNotNull();
      expected.compareWithActual(actual, resolvedData);
    }, "Verify records in cod_inbounds table");
  }

  @Then("DB Core - verify cod_inbounds record is deleted:")
  public void dbOperatorVerifyTheNewCodSoftDeleted(Map<String, String> dataTableRaw) {
    Map<String, String> resolvedData = resolveKeyValues(dataTableRaw);
    CodInbounds expected = new CodInbounds(resolvedData);
    doWithRetry(() -> {
      CodInbounds actual = null;
      if (expected.getRouteId() != null) {
        actual = codInboundsDao.getDeletedCodInboundsByRouteId(expected.getRouteId());
      }
      Assertions.assertThat(actual.getDeletedAt())
          .as("cod_inbounds record was not deleted: " + dataTableRaw)
          .isNotNull();
    }, "Operator verify the COD for created route is soft deleted");
  }

  @When("DB Core - verify orders from {string} records are hard-deleted in waypoints table:")
  public void verifyOrdersWaypointsAreHardDeleted(String listKey, List<String> orderIds) {
    resolveValues(orderIds).forEach(id -> verifyOrderWaypointsAreHardDeleted(id, listKey));
  }

  @When("DB Core - verify {value} order from {string} records are hard-deleted in waypoints table")
  public void verifyOrderWaypointsAreHardDeleted(String orderId, String listKey) {
    List<Order> orders = get(listKey);
    if (CollectionUtils.isEmpty(orders)) {
      throw new IllegalArgumentException("There are no orders stored under [" + listKey + "] list");
    }
    Order order = orders.stream()
        .filter(o -> Objects.equals(o.getId(), Long.valueOf(orderId)))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Order with ID " + orderId + " was not found"));
    Set<Long> waypointIds = order.getTransactions().stream()
        .map(Transaction::getWaypointId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    SoftAssertions assertions = new SoftAssertions();
    waypointIds.forEach(w -> {
      Waypoints waypoint = waypointsDao.getWaypointsDetails(w);
      assertions.assertThat(waypoint)
          .as("waypoints record for id=%s", w)
          .isNull();
    });
    assertions.assertAll();
  }

  @And("DB Core - Operator verifies tags of {string} order:")
  public void verifyOrderTags(String orderId, List<String> data) {
    List<String> expected = resolveValues(data);
    doWithRetry(
        () -> {
          List<OrderTags> actual = orderTagsDao.getOrderTags(Long.parseLong(resolveValue(orderId)));
          Assertions.assertThat(actual).as("List of order_tag records for order_id=%s", orderId)
              .isNotEmpty();
          Assertions.assertThat(actual).extracting(o -> String.valueOf(o.getTagId()))
              .as("List of tag_id for order_id=%s", orderId)
              .containsExactlyInAnyOrderElementsOf(expected);
        }, "verify tags");
  }

  @And("DB Core - Operator verifies order_tags_search record of {string} order:")
  public void verifyOrderTagsSearch(String orderId, Map<String, String> data) {
    Map<String, String> resolvedDataTable = resolveKeyValues(data);
    OrderTagsSearch expected = new OrderTagsSearch(resolvedDataTable);
    doWithRetry(
        () -> {
          OrderTagsSearch actual = orderTagsSearchDao
              .getOrderTagsSearch(Long.parseLong(resolveValue(orderId)));
          expected.compareWithActual(actual, resolveKeyValues(data));
          List<String> actualTags = fromJsonToList(f("[%s]", actual.getOrderTagIds()),
              String.class);
          List<String> expectedTags = fromJsonToList(f("[%s]", resolvedDataTable.get("tagIds")),
              String.class);
          Assertions.assertThat(actualTags)
              .as("Actual tags contains the expected tags: {}", expectedTags)
              .containsAll(expectedTags);
        }, "verify order_tags_search");
  }

  @When("DB Core - verify route_waypoint records are hard-deleted:")
  public void verifyRouteWaypointIsHardDeleted(List<String> data) {
    List<String> resolvedData = resolveValues(data);
    doWithRetry(() ->
            resolvedData.forEach(e -> {
              List<RouteWaypoint> actual = routeWaypointDao.getRouteWaypointsByWaypointId(
                  Long.parseLong(e));
              Assertions.assertThat(actual)
                  .as("Unexpected route_waypoint records were found: %s", actual)
                  .isNullOrEmpty();
            })
        , "verify route_waypoint records", 10_000, 3);
  }

  @When("DB Core - get order by order tracking id {string}")
  public void getOrderDetailByTrackingId(String trackingNumber) {
    String resolvedTrackingNumber = resolveValue(trackingNumber);
    doWithRetry(() -> {
      Orders orders = orderDao.getSingleOrderDetailsByTrackingId(resolvedTrackingNumber);
      Assertions.assertThat(orders)
          .as("Unexpected order not found with tracking number: %s", trackingNumber)
          .isNotNull();
      put(KEY_LIST_OF_CREATED_ORDERS, orders);
    }, "get order record", 10_000, 3);
  }

  @And("DB Core - Operator verifies cod_collections record:")
  public void verifyCodCollections(Map<String, String> data) {
    CodCollections expected = new CodCollections(resolveKeyValues(data));
    doWithRetry(() -> {
      var actual = codCollectionDao.getMultipleCodCollections(expected.getWaypointId());
      Assertions.assertThat(actual)
          .as("List of cod_collections records for waypointId=%s", expected.getWaypointId())
          .isNotEmpty();
      Assertions.assertThat(actual)
          .as("List of cod_collections records for waypointId=%s", expected.getWaypointId())
          .anyMatch(expected::matchedTo);
    }, "verify cod_collections");
  }

  @And("DB Core - Operator verifies inbound_scans record:")
  public void verifyInboundScans(Map<String, String> data) {
    InboundScans expected = new InboundScans(resolveKeyValues(data));
    doWithRetry(() -> {
      var actual = inboundScansDao.findInboundScansByOrderId(expected.getOrderId());
      Assertions.assertThat(actual)
          .as("List of inbound_scans records for orderId=%s", expected.getOrderId())
          .isNotEmpty()
          .anyMatch(expected::matchedTo);
      putInList(KEY_CORE_LIST_OF_INBOUND_SCANS, actual);
    }, "Fetch InboundScans", 2000, 3);
  }

  @And("DB Core - verify orders records are hard-deleted in transactions table:")
  public void verifyTransactionsAreHardDeleted(List<String> data) {
    List<String> resolvedData = resolveValues(data);
    doWithRetry(() ->
            resolvedData.forEach(e -> {
              List<Transactions> actual = transactionsDao.getMultipleTransactionsByOrderId(
                  Long.parseLong(e));
              Assertions.assertThat(actual)
                  .as("Unexpected transactions records were found: %s", actual)
                  .isNullOrEmpty();
            })
        , "verify transactions records", 10_000, 3);
  }

  @And("DB Core - verify orders records are hard-deleted in order_details table:")
  public void verifyOrderDetailsAreHardDeleted(List<String> data) {
    List<String> resolvedData = resolveValues(data);
    doWithRetry(() ->
            resolvedData.forEach(e -> {
              List<OrderDetails> actual = orderDetailsDao.getMultipleOrderDetailsByOrderId(
                  Long.parseLong(e));
              Assertions.assertThat(actual)
                  .as("Unexpected order_details records were found: %s", actual)
                  .isNullOrEmpty();
            })
        , "verify order_details records", 10_000, 3);
  }

  @And("DB Core - verify orders records are hard-deleted in order_delivery_verifications table:")
  public void verifyOrderDeliveryVerificationsAreHardDeleted(List<String> data) {
    List<String> resolvedData = resolveValues(data);
    doWithRetry(() ->
            resolvedData.forEach(e -> {
              OrderDeliveryVerifications actual = orderDeliveryVerificationsDao.getTransactionBlob(
                  Long.parseLong(e));
              Assertions.assertThat(actual)
                  .as("Unexpected order_delivery_verifications records were found: %s", actual)
                  .isNull();
            })
        , "verify order_delivery_verifications records", 10_000, 3);
  }

  @And("DB Core - get order_delivery_details record for order {value}")
  public void getOrderDeliveryDetailsRecord(String orderId) {
    doWithRetry(() -> {
      var result = orderDeliveryDetailsDao.getOrderDeliveryDetails(Long.parseLong(orderId));
      putInList(KEY_CORE_LIST_OF_ORDER_DELIVERY_DETAILS, result);
    }, "get order_delivery_details record", 10_000, 3);
  }

  @Given("DB Core - soft delete route {value}")
  public void dbOperatorSoftDeleteRoute(String routeId) {
    routeDbDao.softDeleteRoute(resolveValue(routeId));
  }

  /**
   * Sample:<p> Then DB Core - verifies service_level in orders table<p>
   * |orderId|{KEY_LIST_OF_CREATED_ORDERS[1].id}|<p> |serviceLevel|NEXTDAY|<p>
   * <p>
   * Service Level : can be NEXTDAY or STANDARD
   *
   * @param dataTable
   */
  @When("DB Core - verifies service_level in orders table")
  public void operatorFindOrdersServiceLevel(Map<String, String> dataTable) {
    Map<String, String> resolvedData = resolveKeyValues(dataTable);
    long orderId = Long.parseLong(resolvedData.get("orderId"));
    String expectedServiceType = resolvedData.get("serviceLevel");
    String actualServiceType = orderDetailsDao.getOrdersServiceLevel(orderId);
    Assertions.assertThat(actualServiceType).as("orders.service_level equal")
        .isEqualTo(expectedServiceType);
  }
}