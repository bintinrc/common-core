package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.OrderDao;
import co.nvqa.common.core.hibernate.ReservationsDao;
import co.nvqa.common.core.hibernate.WaypointsDao;
import co.nvqa.common.core.model.persisted_class.Reservations;
import co.nvqa.common.core.model.persisted_class.Waypoints;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;

public class DbCoreSteps extends CoreStandardSteps {

  @Inject
  private OrderDao orderDao;

  @Inject
  private WaypointsDao waypointsDao;

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
      List<Reservations> results = reservationDao.getReservationsDetailsByReservationId(resolvedReservationIdKey);
      put(KEY_WAYPOINT_ID, results.get(0).getWaypointId());
    }, "Validating verified WayPoint Id value is as expected", 2000, 3);
  }

  @Then("DB Core - verifies that zone type is equal to {string} and zone id is not null for waypointId {string}")
  public void dbCoreVerifiesThatZoneIdEqualTo(String expectedZoneType , String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      List<Waypoints> results = waypointsDao.getWaypointsDetailsByWaypointId(resolvedWayPointIdKey);
      Assertions.assertThat(results.get(0).getZoneType())
          .as("Assertion for Zone Type column value is as expected").isEqualTo(expectedZoneType);
      Assertions.assertThat(results.get(0).getRoutingZoneId())
          .as("Assertion for Zone Id column value is as expected").isNotNull();
    }, "Validating verified Zone Type value is as expected", 2000, 3);
  }

  @Then("DB Core - verifies that zone type is equal to {string} and zone id is null for waypointId {string}")
  public void dbCoreVerifiesThatZoneIdEqualToNull(String expectedZoneType , String waypointId) {
    Long resolvedWayPointIdKey = Long.parseLong(resolveValue(waypointId));
    retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      List<Waypoints> results = waypointsDao.getWaypointsDetailsByWaypointId(resolvedWayPointIdKey);
      Assertions.assertThat(results.get(0).getZoneType())
          .as("Assertion for Zone Type column value is as expected").isEqualTo(expectedZoneType);
      Assertions.assertThat(results.get(0).getRoutingZoneId())
          .as("Assertion for Zone Id column value is as expected").isNull();
    }, "Validating verified Zone Type value is as expected", 2000, 3);
  }
}
