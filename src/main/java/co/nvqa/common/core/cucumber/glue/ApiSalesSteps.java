package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.SalesClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.miscellanous.SalesPerson;
import co.nvqa.common.core.utils.CoreTestUtils;
import io.cucumber.java.en.Given;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class ApiSalesSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private SalesClient salesClient;

  @Given("API Core - Operator create sales person:")
  public void apiOperatorCreateSalesPerson(Map<String, String> data) {
    SalesPerson salesPerson = new SalesPerson(resolveKeyValues(data));
    String uniqueString = CoreTestUtils.generateUniqueId();
    if (StringUtils.endsWithIgnoreCase(salesPerson.getName(), "{uniqueString}")) {
      salesPerson.setName(salesPerson.getName().replace("{uniqueString}", uniqueString));
    }
    if (StringUtils.endsWithIgnoreCase(salesPerson.getCode(), "{uniqueString}")) {
      salesPerson.setCode(salesPerson.getCode().replace("{uniqueString}", uniqueString));
    }
    salesPerson = getSalesClient().createSalesPerson(salesPerson);
    putInList(KEY_CORE_LIST_OF_SALES_PERSON, salesPerson);
  }

}
