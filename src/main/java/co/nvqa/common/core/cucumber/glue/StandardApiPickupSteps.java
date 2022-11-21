package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.PickupClient;
import co.nvqa.common.core.client.ReservationClient;
import co.nvqa.common.core.model.pickup.Pickup;
import co.nvqa.common.core.utils.CoreScenarioStorageKeys;
import co.nvqa.common.cucumber.StandardScenarioManager;
import co.nvqa.common.cucumber.glue.StandardSteps;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.java.en.When;
import java.util.List;

public class StandardApiPickupSteps extends StandardSteps<StandardScenarioManager> implements
    CoreScenarioStorageKeys {

  private PickupClient pickupClient;

  @Override
  public void init() {

  }

  @When("API Operator get pickup from reservation id {string}")
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
