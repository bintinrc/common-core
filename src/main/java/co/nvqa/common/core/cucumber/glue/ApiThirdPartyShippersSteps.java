package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.ThirdPartyShippersClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.ThirdPartyShippers;
import io.cucumber.java.en.Given;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


public class ApiThirdPartyShippersSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private ThirdPartyShippersClient thirdPartyShippersClient;

  @Given("API Core - Operator gets data of {string} Third Party shipper")
  public void apiOperatorGetsDataOfCreatedThirdPartyShipper(String createdShipper) {
    ThirdPartyShippers thirdPartyShipper = resolveValue(createdShipper);
    doWithRetry(() -> {
      List<ThirdPartyShippers> thirdPartyShippers = getThirdPartyShippersClient().getAll();
      ThirdPartyShippers apiData = thirdPartyShippers.stream()
          .filter(shipper -> StringUtils.equals(shipper.getName(), thirdPartyShipper.getName()))
          .findFirst()
          .orElseThrow(() -> new RuntimeException(
              f("Third Party Shipper with name [%s] was not found",
                  thirdPartyShipper.getName())));
      thirdPartyShipper.setId(apiData.getId());
    }, "get All third party shippers");
  }
}
