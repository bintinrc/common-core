package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.CoreDpClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import io.cucumber.java.en.And;
import javax.inject.Inject;
import lombok.Getter;

public class ApiDpSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private CoreDpClient coreDpClient;

  @Override
  public void init() {

  }

  /**
   * API Core - Operator perform dp drop off with order id "{string}"
   *
   * @param orderIdString      example: {KEY_LIST_OF_CREATED_ORDERS[1].id}
   */
  @And("API Core - Operator perform dp drop off with order id {string}")
  public void operatorPerformDpDropOff(String orderIdString) {
    Long orderId = Long.parseLong(resolveValue(orderIdString));

    retryIfAssertionErrorOrRuntimeExceptionOccurred(
        () -> getCoreDpClient().driverDropOffToDp(orderId), "core drop off dp");
  }
}
