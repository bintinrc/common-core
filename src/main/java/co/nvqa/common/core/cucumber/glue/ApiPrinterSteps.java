package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.PrintersClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.PrinterSettings;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;

@ScenarioScoped
public class ApiPrinterSteps extends CoreStandardSteps {

  @Inject
  @Getter
  private PrintersClient printersClient;

  /**
   * sample of parameter in dataTableAsMap from feature file.
   *
   * @param data <br><b>name:</b>
   *             new Printer <br><b>ipAddress:</b> 127.0.0.1:9000<br>
   *             <b>version</b><b>3</b><br>
   *             <b>isDefault</b>true
   */
  @Given("API Core - Operator adds new printer using data below:")
  public void apiOperatorAddsNewPrinter(Map<String, String> data) {
    Map<String, String> dataMap = resolveKeyValues(data);
    doWithRetry(() -> {
      PrinterSettings printerSettings = new PrinterSettings(dataMap);
      printerSettings = getPrintersClient().addPrinter(printerSettings);
      put(KEY_CORE_PRINTER_SETTINGS, printerSettings);
    }, "add new printer");
  }
}
