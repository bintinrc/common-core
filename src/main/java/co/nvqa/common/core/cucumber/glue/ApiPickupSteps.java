package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.PickupClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.pickup.Pickup;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.When;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;

@ScenarioScoped
public class ApiPickupSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private PickupClient pickupClient;

  @Override
  public void init() {

  }

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
}
