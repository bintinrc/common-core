package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.DebugClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import lombok.Getter;
import org.assertj.core.api.Assertions;

@ScenarioScoped
public class ApiDebugSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private DebugClient debugClient;


  @Then("API Core - verify driver's total cod:")
  public void verifyTotalCod(Map<String, String> data) {
    Map<String, String> resolvedDataTable = resolveKeyValues(data);
    final long driverId = Long.parseLong(resolvedDataTable.get("driverId"));
    final String routeDate = resolvedDataTable.get("routeDate");
    final double expected = Double.parseDouble(resolvedDataTable.get("cod"));

    Optional<String> refresh = data.containsKey("refresh") ?
        Optional.of((resolvedDataTable.get("refresh"))) : Optional.of("false");

    doWithRetry(
        () -> {
          var result = getDebugClient().getTotalCod(driverId, routeDate, refresh.orElse("false"));
          Assertions.assertThat(Double.valueOf(result.get("data").toString()))
              .as("Total COD")
              .isEqualTo(expected);
        }, "verify total cod");
  }

  @Then("API Core - evict driver's cod limit cache:")
  public void evictDriverCodLimitCache(Map<String, String> data) {
    Map<String, String> resolvedDataTable = resolveKeyValues(data);
    final long driverId = Long.parseLong(resolvedDataTable.get("driverId"));

    doWithRetry(
        () -> getDebugClient().evictDriverCodLimitCache(driverId),
        "evict driver cod limit cache");
  }
}
