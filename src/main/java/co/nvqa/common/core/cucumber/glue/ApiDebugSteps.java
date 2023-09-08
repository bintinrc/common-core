package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.DebugClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import java.util.Map;
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
    data = resolveKeyValues(data);
    long driverId = Long.parseLong(data.get("driverId"));
    String routeDate = data.get("routeDate");
    double expected = Double.parseDouble(data.get("cod"));
    doWithRetry(
        () -> {
          var result = getDebugClient().getTotalCod(driverId, routeDate);
          Assertions.assertThat(Double.valueOf(result.get("data").toString()))
              .as("Total COD")
              .isEqualTo(expected);
        }, "verify total cod");
  }
}
