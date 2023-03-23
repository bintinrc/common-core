package co.nvqa.common.core.client;

import co.nvqa.common.core.model.OrderJaroScoresV2Info;
import co.nvqa.common.utils.NvRetry;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.resource.PropertySource;

@ExtendWith(SystemStubsExtension.class)
public class OrderJaroScoresV2ClientTest implements NvRetry {

  @SystemStub
  private EnvironmentVariables env;

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderJaroScoresV2ClientTest.class);

  OrderJaroScoresV2Client testOrderJaroScoresV2Client = null;

  OrderJaroScoresV2Info testOrderJaroScoresV2Info = null;


  @BeforeEach
  void setUp() throws Exception {
    env = new EnvironmentVariables(PropertySource.fromResource(".env"));
    env.execute(() -> {
      testOrderJaroScoresV2Client = new OrderJaroScoresV2Client();
      testOrderJaroScoresV2Info = new OrderJaroScoresV2Info();
    });
  }

}