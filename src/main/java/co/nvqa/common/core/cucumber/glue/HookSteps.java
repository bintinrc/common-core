package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.ReservationClient;
import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.reservation.ReservationRequest;
import co.nvqa.common.core.model.reservation.ReservationResponse;
import co.nvqa.common.core.model.route.RouteResponse;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import io.cucumber.java.Before;
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

  @Inject
  @Getter
  private ReservationClient reservationClient;

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
        doWithRetry(() -> getRouteClient().archiveRoute(r.getId()),
            "After hook: @ArchiveRouteCommonV2");
        LOGGER.debug("Route ID = {} archived successfully", r.getId());
      } catch (Throwable t) {
        LOGGER.warn("error to archive route: " + t.getMessage());
      }
    });
  }

  @After("@CancelCreatedReservations")
  public void cancelReservation() {
    final List<ReservationResponse> reservations = get(KEY_LIST_OF_CREATED_RESERVATIONS);
    if (Objects.isNull(reservations) || reservations.isEmpty()) {
      LOGGER.trace(
          "no reservations have been created under key \"KEY_LIST_OF_CREATED_RESERVATIONS\"");
      return;
    }
    reservations.forEach(r -> {
      try {
        getReservationClient().cancelReservation(r.getId());
        LOGGER.debug("Reservation ID = {} cancelled successfully", r.getId());
      } catch (Throwable t) {
        LOGGER.warn("error to cancel reservation: " + t.getMessage());
      }
    });
  }
}
