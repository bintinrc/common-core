package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.ParameterClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.utils.JsonUtils;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class ApiParameterSteps extends CoreStandardSteps {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiParameterSteps.class);

  @Inject
  @Getter
  private ParameterClient parameterClient;


  @When("API Core - set system parameter:")
  public void getPickupFromReservationId(Map<String, String> data) {
    doWithRetry(
        () -> parameterClient.setParameters(JsonUtils.toJson(resolveKeyValues(data))),
        "set system parameter");
  }

  @After("@RestoreSystemParams")
  public void restoreSystemParams() {
    var params = List.of(
        Map.of(
            "key", "APPLY_DRIVER_NUMBER_OF_ROUTE_LIMIT",
            "value", 0),
        Map.of(
            "key", "IS_DRIVER_COD_LIMIT_APPLIED",
            "value", 1),
        Map.of(
            "key", "DRIVER_DAILY_COD_LIMIT",
            "value", 30000000)
    );
    params.forEach(data -> {
      try {
        doWithRetry(() -> parameterClient.setParameters(JsonUtils.toJson(data)),
            "Running hook @RestoreSystemParams");
      } catch (Exception ex) {
        LOGGER.warn("Could not set system parameter: {}", data, ex);
      }
    });
  }
}