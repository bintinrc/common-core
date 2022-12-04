package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.PickupClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.pickup.Pickup;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.java.en.When;
import java.util.List;

public class ApiPickupSteps extends CoreStandardSteps {

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
    final List<Pickup> pickups = getPickupClient().getPickups(reservationId);
    put(KEY_LIST_OF_PICKUPS, pickups);
  }

  private PickupClient getPickupClient() {
    if (pickupClient == null) {
      pickupClient = new PickupClient(StandardTestConstants.API_BASE_URL,
          TokenUtils.getOperatorAuthToken());
    }

    return pickupClient;
  }
}
