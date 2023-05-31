package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.client.ReservationClient;
import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.reservation.ReservationResponse;
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

  @Inject
  @Getter
  private OrderClient orderClient;

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
    final List<ReservationResponse> reservationResponses = get(KEY_LIST_OF_RESERVATIONS);
    if (!Objects.isNull(reservations) && !reservations.isEmpty()) {
      reservations.forEach(r -> {
        try {
          getReservationClient().cancelReservation(r.getId());
          LOGGER.debug("Reservation ID = {} cancelled successfully", r.getId());
        } catch (Throwable t) {
          LOGGER.warn("error to cancel reservation: " + t.getMessage());
        }
      });
    }

    if (!Objects.isNull(reservationResponses) && !reservationResponses.isEmpty()) {
      reservationResponses.forEach(r -> {
        try {
          getReservationClient().cancelReservation(r.getId());
          LOGGER.debug("Reservation ID = {} cancelled successfully", r.getId());
        } catch (Throwable t) {
          LOGGER.warn("error to cancel reservation: " + t.getMessage());
        }
      });
    }
  }

  @After("@ForceSuccessCommonV2")
  public void forceSuccess() {
    final List<Order> orders = get(KEY_LIST_OF_CREATED_ORDERS);
    if (Objects.isNull(orders) || orders.isEmpty()) {
      LOGGER.trace(
          "no routes been created under key \"KEY_LIST_OF_CREATED_ORDERS\", skip the force success");
      return;
    }
    orders.forEach(o -> {
      try {
        doWithRetry(() -> getOrderClient().forceSuccess(o.getId(), true),
            "After hook: @ForceSuccess");
        LOGGER.debug("Order ID = {} force successfully", o.getId());
      } catch (Throwable t) {
        LOGGER.warn("Error to force success: " + t.getMessage());
      }
    });
  }

  @After("@DeleteRoutes")
  public void deleteRoutes() {
    final List<RouteResponse> routes = get(KEY_LIST_OF_CREATED_ROUTES);
    if (Objects.isNull(routes) || routes.isEmpty()) {
      LOGGER.trace(
          "no routes been created under key \"KEY_LIST_OF_CREATED_ROUTES\", skip the delete routes");
      return;
    }
    routes.forEach(r -> {
      try {
        doWithRetry(() -> getRouteClient().deleteRoute(r.getId()),
            "After hook: @DeleteRoutes");
        LOGGER.debug("Route ID = {} deleted successfully", r.getId());
      } catch (Throwable t) {
        LOGGER.warn("error to delete route: " + t.getMessage());
      }
    });
  }
}
