package co.nvqa.common.core.client;

import co.nvqa.common.core.model.route_monitoring.RouteMonitoringResponse;
import co.nvqa.common.utils.DateUtil;
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
public class RouteMonitoringClientTest implements NvRetry {

  @SystemStub
  private EnvironmentVariables env;

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteMonitoringClientTest.class);

  RouteMonitoringClient testRouteMonitoringClient = null;

  RouteMonitoringResponse testRouteMonitoringResponse = null;

  @BeforeEach
  void setUp() throws Exception {
    env = new EnvironmentVariables(PropertySource.fromResource(".env"));
    env.execute(() -> {
      testRouteMonitoringClient = new RouteMonitoringClient();
      testRouteMonitoringResponse = new RouteMonitoringResponse();
    });
  }

  @Test
  void test_getRouteMonitoringDetails() throws Exception {
    env.execute(() -> {
      String date = DateUtil.getTodayDate_YYYY_MM_DD();
      List<Long> hubIds = List.of(15617L);
      List<Long> zoneIds = List.of(29259L);
      List<RouteMonitoringResponse> response = testRouteMonitoringClient.getRouteMonitoringDetails(
          date, hubIds, zoneIds, 500);
      Assertions.assertThat(response).isNotNull();
      response.forEach(routeMonitoringResponse -> LOGGER.info(routeMonitoringResponse.toString()));
    });
  }

  @Test
  void test_getParcelDetails() throws Exception {
    env.execute(() -> {
      String date = DateUtil.getTodayDate_YYYY_MM_DD();
      List<Long> hubIds = List.of(15617L);
      List<Long> zoneIds = List.of(29259L);
      List<RouteMonitoringResponse> r = testRouteMonitoringClient.getRouteMonitoringDetails(
          date, hubIds, zoneIds, 500);
      Long testRoute = r.get(0).getRouteId();
      RouteMonitoringResponse response = testRouteMonitoringClient.getParcelDetails(
          testRoute, "", "dd");
      Assertions.assertThat(response).isNotNull();
    });
  }

}