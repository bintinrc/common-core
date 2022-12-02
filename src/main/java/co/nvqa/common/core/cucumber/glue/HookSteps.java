package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class HookSteps extends CoreStandardSteps {

  private static final Logger LOGGER = LoggerFactory.getLogger(HookSteps.class);

  private RouteClient routeClient;

  @Override
  public void init() {

  }

  @After("@ArchiveRouteCommonV2")
  public void archiveRoute() {
    final List<RouteResponse> routes = get(KEY_LIST_OF_CREATED_ROUTES);

    routes.forEach(r -> {
      try {
        getRouteClient().archiveRouteV2(r.getId());
        LOGGER.debug("Route ID = {} archived successfully", r.getId());
      } catch (Throwable t) {
        LOGGER.warn("error to archive route: " + t.getMessage());
      }
    });
  }

  private RouteClient getRouteClient() {
    if (routeClient == null) {
      routeClient = new RouteClient(StandardTestConstants.API_BASE_URL,
          TokenUtils.getOperatorAuthToken());
    }

    return routeClient;
  }

}
