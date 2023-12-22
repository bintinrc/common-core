package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.PickupClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.pickup.Pickup;
import co.nvqa.common.core.model.pickup.PickupSearchRequest;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class ApiPickupSteps extends CoreStandardSteps {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiPickupSteps.class);

  @Inject
  @Getter
  private PickupClient pickupClient;

  /**
   * <br/><b>Output key</b>: <ul><li>KEY_LIST_OF_PICKUPS: list of pickups</li></ul>
   * <p>
   * This method will read all the pickup in the given reservation id, and replace the previous
   * value on the scenario storage
   *
   * @param reservationIdString key contains core's reservation id
   */
  @When("API Core - Operator get pickup from reservation id {string}")
  public void getPickupFromReservationId(String reservationIdString) {
    final long reservationId = Long.parseLong(resolveValue(reservationIdString));
    doWithRetry(
        () -> {
          final List<Pickup> pickups = getPickupClient().getPickupById(reservationId);
          put(KEY_LIST_OF_PICKUPS, pickups);
        }, "get pickup details");
  }

  /**
   * Sample:
   * <p>
   * When API Core - Operator search pickup using data below:
   * <br/>| requestJson | {"reservation_ids":[111111,111122] } |
   *
   * @param dataTableAsMap Map of data from feature file.
   */
  @When("API Core - Operator search pickup using data below:")
  public void searchPickup(Map<String, String> dataTableAsMap) {
    String resolvedJsonRequest = resolveValue(dataTableAsMap.get("requestJson"));
    PickupSearchRequest request = getPickupClient().fromJson(resolvedJsonRequest,
        PickupSearchRequest.class);
    doWithRetry(() -> {
      final List<Pickup> pickups = getPickupClient().searchPickupsWithFilters(request);
      putAllInList(KEY_CORE_LIST_OF_PICKUPS, pickups);
    }, "Search Pickup");
  }

  @When("API Core - Operator verify pods in pickup details of reservation id below:")
  public void getPickupWithPodDetailsFromReservationId(Map<String, String> dataTable) {
    Map<String, String> resolvedData = resolveKeyValues(dataTable);
    Pickup expectedPickup = new Pickup(resolvedData);

    doWithRetry(() -> {
      final Pickup actualPickup = getPickupClient().getPickupWithPod(expectedPickup.getId());
      Assertions.assertThat(actualPickup.getPods())
          .as("Pod details not found: " + resolvedData)
          .isNotNull();
      LOGGER.info(actualPickup.toString());
      expectedPickup.compareWithActual(actualPickup);
    }, "get pickup with pod details");
  }

  @When("API Core - Operator verify there is no pods assigned to reservation id {string}")
  public void verifyNoPodDetailsFromReservationId(String reservationIdString) {
    final long reservationId = Long.parseLong(resolveValue(reservationIdString));

    doWithRetry(() -> {
      final Pickup pickups = getPickupClient().getPickupWithPod(reservationId);
      LOGGER.info(pickups.toString());
      Assertions.assertThat(pickups.getPods().size())
          .as("there should be no pods assigned to reservation id").isZero();
    }, "verify there is no pods assigned to reservation id");
  }
}
