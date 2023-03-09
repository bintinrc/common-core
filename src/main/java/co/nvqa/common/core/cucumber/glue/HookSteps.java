package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.route.RouteResponse;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class HookSteps extends CoreStandardSteps {

  private static final Logger LOGGER = LoggerFactory.getLogger(HookSteps.class);

  @Inject
  @Getter
  private RouteClient routeClient;

  @Override
  public void init() {

  }

  @After("@ArchiveRouteCommonV2")
  public void archiveRoute() {
    final List<RouteResponse> routes = get(KEY_LIST_OF_CREATED_ROUTES);
    if (Objects.isNull(routes) || routes.isEmpty()) {
      LOGGER.trace(
          "no routes been created under key \"KEY_LIST_OF_CREATED_ROUTES\", skip the route archival");
      return;
    }
    routes.forEach(r -> {
      try {
        getRouteClient().archiveRoute(r.getId());
        LOGGER.debug("Route ID = {} archived successfully", r.getId());
      } catch (Throwable t) {
        LOGGER.warn("error to archive route: " + t.getMessage());
      }
    });
  }
}
