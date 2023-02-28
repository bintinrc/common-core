package co.nvqa.common.core.client;

import co.nvqa.common.core.model.OutboundRequest;
import co.nvqa.common.utils.NvRetry;
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
public class OutboundClientTest implements NvRetry {

  @SystemStub
  private EnvironmentVariables env;

  private static final Logger LOGGER = LoggerFactory.getLogger(PrintersClientTest.class);

  OutboundClient testOutboundClient = null;
  OutboundRequest testOutboundRequest = null;

  @BeforeEach
  void setUp() throws Exception {
    env = new EnvironmentVariables(PropertySource.fromResource(".env"));
    env.execute(() -> {
      testOutboundClient = new OutboundClient();
      testOutboundRequest = new OutboundRequest();
    });
  }

  @Test
  void test_outboundScan() throws Exception {
    env.execute(() -> {
      testOutboundRequest.setHubId(1L);
      testOutboundRequest.setRouteId(95838506L);
      testOutboundRequest.addSuccessScans("NVSGAPMQA000114077");
      testOutboundClient.outboundScan(testOutboundRequest);
    });
  }

  @Test
  void test_confirmScans() throws Exception {
    env.execute(() -> {
      testOutboundRequest.setHubId(1L);
      testOutboundRequest.setRouteId(95838506L);
      testOutboundRequest.addSuccessScans("NVSGAPMQA000114077");
      testOutboundClient.confirmScans(testOutboundRequest);
    });
  }
}