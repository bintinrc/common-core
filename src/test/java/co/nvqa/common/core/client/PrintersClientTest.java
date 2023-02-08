package co.nvqa.common.core.client;

import co.nvqa.common.core.model.PrinterSettings;
import co.nvqa.common.utils.NvRetry;
import co.nvqa.common.utils.NvTestRuntimeException;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.resource.PropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(SystemStubsExtension.class)
public class PrintersClientTest implements NvRetry {

  @SystemStub
  private EnvironmentVariables env;

  private static final Logger LOGGER = LoggerFactory.getLogger(PrintersClientTest.class);

  PrintersClient printersClient = null;

  PrinterSettings testPrinterSettings = null;

  @BeforeEach
  void setUp() throws Exception {
    env = new EnvironmentVariables(PropertySource.fromResource(".env"));
    env.execute(() -> {
      printersClient = new PrintersClient();
      testPrinterSettings = new PrinterSettings();
    });
  }

  @Test
  void test_addPrinter() throws Exception {
    env.execute(() -> {
      testPrinterSettings.setName("Unit Test Printer");
      testPrinterSettings.setIpAddress("1.2.3.4");
      testPrinterSettings.setVersion(1);
      testPrinterSettings.setDefault(false);
      PrinterSettings response = printersClient.addPrinter(testPrinterSettings);
      Assertions.assertThat(response).isNotNull();
      LOGGER.info(String.format("addPrinter Response: %s: ", response));
    });
  }

  @Test
  void test_getAll() throws Exception {
    env.execute(() -> {
      List<PrinterSettings> printers = printersClient.getAll();
      Assertions.assertThat(printers).isNotEmpty();
      LOGGER.info(String.format("getAll Response: %s", printers));
    });
  }

  @Test
  void test_delete() throws Exception {
    env.execute(() -> {
      test_addPrinter();
      retryIfRuntimeExceptionOccurred(
          () -> {
            List<PrinterSettings> allPrinters = printersClient.getAll();
            List<PrinterSettings> testPrinters = allPrinters.stream().filter(
                printer -> printer.getName().equals("Unit Test Printer")
            ).collect(Collectors.toList());
            Assertions.assertThat(testPrinters).isNotEmpty();
            testPrinters.forEach(
                printer -> {
                  try {
                    printersClient.delete(printer.getId());
                  } catch (Exception e) {
                    throw new NvTestRuntimeException(e);
                  }
                  LOGGER.info(String.format("printerId: %s is deleted", printer.getId()));
                }
            );
          }
      );
    });
  }
}