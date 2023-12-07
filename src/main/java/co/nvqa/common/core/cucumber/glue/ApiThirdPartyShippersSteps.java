package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.ThirdPartyShippersClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.ThirdPartyShippers;
import io.cucumber.java.en.Given;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;


public class ApiThirdPartyShippersSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private ThirdPartyShippersClient thirdPartyShippersClient;


  @Given("API Core - Operator verify the {string} Third Party Shipper is searchable")
  public void apiOperatorGetsDataOfCreatedThirdPartyShipper(String expectedShipperName) {
    String resolvedExpectedShipperName = resolveValue(expectedShipperName);
    doWithRetry(() -> {
      List<ThirdPartyShippers> allThirdPartyShippers = getThirdPartyShippersClient().getAll();

      List<ThirdPartyShippers> filteredThirdPartyShipper = allThirdPartyShippers.stream()
          .filter(shipper -> StringUtils.equals(shipper.getName(), resolvedExpectedShipperName))
          .findFirst().stream().collect(Collectors.toList());

      Assertions.assertThat(filteredThirdPartyShipper).isNotEmpty();
    }, "get third party shippers");
  }
}
