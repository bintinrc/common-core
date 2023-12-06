package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.client.CoreNotificationsClient;
import co.nvqa.common.core.client.OrderClient;
import co.nvqa.common.core.client.PrintersClient;
import co.nvqa.common.core.client.ReservationClient;
import co.nvqa.common.core.client.RouteClient;
import co.nvqa.common.core.client.SalesClient;
import co.nvqa.common.core.client.TagClient;
import co.nvqa.common.core.client.ThirdPartyShippersClient;
import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.RouteDbDao;
import co.nvqa.common.core.model.PrinterSettings;
import co.nvqa.common.core.model.RouteGroup;
import co.nvqa.common.core.model.SmsNotificationsSettings;
import co.nvqa.common.core.model.ThirdPartyShippers;
import co.nvqa.common.core.model.coverage.CreateCoverageResponse;
import co.nvqa.common.core.model.miscellanous.SalesPerson;
import co.nvqa.common.core.model.order.Order;
import co.nvqa.common.core.model.order.OrderTag;
import co.nvqa.common.core.model.persisted_class.route.Coverage;
import co.nvqa.common.core.model.persisted_class.route.RouteLogs;
import co.nvqa.common.core.model.reservation.ReservationResponse;
import co.nvqa.common.core.model.route.RouteResponse;
import co.nvqa.common.core.model.route.RouteTag;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

  @Inject
  @Getter
  private SalesClient salesClient;

  @Inject
  @Getter
  private RouteDbDao routeDbDao;

  @Inject
  @Getter
  private TagClient tagClient;
  @Inject
  @Getter
  private CoreNotificationsClient notificationsClient;
  @Inject
  @Getter
  private PrintersClient printersClient;
  @Inject
  @Getter
  private ThirdPartyShippersClient thirdPartyShippersClient;


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
        LOGGER.warn("error to archive route: {}", t.getMessage());
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
          getReservationClient().updateReservation(r.getId(), 4);
          LOGGER.debug("Reservation ID = {} cancelled successfully", r.getId());
        } catch (Throwable t) {
          LOGGER.warn("error to cancel reservation: {}", t.getMessage());
        }
      });
    }

    if (!Objects.isNull(reservationResponses) && !reservationResponses.isEmpty()) {
      reservationResponses.forEach(r -> {
        try {
          getReservationClient().updateReservation(r.getId(), 4);
          LOGGER.debug("Reservation ID = {} cancelled successfully", r.getId());
        } catch (Throwable t) {
          LOGGER.warn("error to cancel reservation: {}", t.getMessage());
        }
      });
    }
  }

  @After("@ForceSuccessCommonV2")
  public void forceSuccess() {
    final List<Order> orders = get(KEY_LIST_OF_CREATED_ORDERS);
    if (Objects.isNull(orders) || orders.isEmpty()) {
      LOGGER.trace(
          "no orders been created under key \"KEY_LIST_OF_CREATED_ORDERS\", skip the force success");
      return;
    }
    orders.forEach(o -> {
      try {
        doWithRetry(() -> getOrderClient().forceSuccess(o.getId(), true),
            "After hook: @ForceSuccessCommonV2");
        LOGGER.debug("Order ID = {} force successfully", o.getId());
      } catch (Throwable t) {
        LOGGER.warn("Error to force success: {}", t.getMessage());
      }
    });
  }

  @After("@DeleteRoutes")
  public void deleteRoutes() {
    List<Object> routes = get(KEY_LIST_OF_CREATED_ROUTES);
    if (Objects.isNull(routes) || routes.isEmpty()) {
      LOGGER.trace(
          "no routes been created under key \"KEY_LIST_OF_CREATED_ROUTES\", skip the delete routes");
      return;
    }
    routes.forEach(r -> {
      try {
        var id = r instanceof RouteResponse ? ((RouteResponse) r).getId() : ((RouteLogs) r).getId();
        doWithRetry(() -> getRouteClient().deleteRoute(id),
            "After hook: @DeleteRoutes");
        LOGGER.debug("Route ID = {} deleted successfully", id);
      } catch (Throwable t) {
        LOGGER.warn("error to delete route: {}", t.getMessage());
      }
    });
  }

  @After("@DeleteCoverageV2")
  public void deleteCoverageV2() {
    final List<CreateCoverageResponse.Data> coveragesResponse = get(KEY_LIST_OF_COVERAGE);
    final List<String> coverages = get(KEY_LIST_OF_CREATED_AREAS);
    final Long coverageid = get(KEY_COVERAGE_ID);
    if (!Objects.isNull(coveragesResponse) && !coveragesResponse.isEmpty()) {
      coveragesResponse.forEach(r -> {
        try {
          getRouteClient().deleteCoverage(r.getId());
          LOGGER.debug("Coverages ID = {} delete successfully", r.getId());
        } catch (Throwable t) {
          LOGGER.warn("error to delete coveraged: {}", t.getMessage());
        }
      });
    }

    if (CollectionUtils.isNotEmpty(coverages)) {
      Set<String> uniqueAreas = new HashSet<>(coverages);
      uniqueAreas.forEach(area -> {
        List<Coverage> actual = routeDbDao.getCoverageByArea(area);
        actual.forEach(c -> {
          try {
            getRouteClient().deleteCoverage(c.getId());
          } catch (Throwable ex) {
            LOGGER.warn("Could not delete coverage {}", c.getId(), ex);
          }
        });
      });
    }

    if (coverageid != null) {
      try {
        doWithRetry(() -> getRouteClient().deleteCoverage(coverageid),
            "After hook: @DeleteCoverageV2");
      } catch (Throwable ex) {
        LOGGER.warn("could not delete coverage {}", coverageid, ex);
      }
    }
  }

  @After("@DeleteRouteGroupsV2")
  public void deleteRouteGroups() {
    List<RouteGroup> routeGroups = get(KEY_LIST_OF_CREATED_ROUTE_GROUPS);
    if (CollectionUtils.isNotEmpty(routeGroups)) {
      List<RouteGroup> allRouteGroups = new ArrayList<>();
      routeGroups.forEach(routeGroup -> {
        try {
          if (routeGroup.getId() != null) {
            getRouteClient().deleteRouteGroup(routeGroup.getId());
          } else {
            if (allRouteGroups.isEmpty()) {
              allRouteGroups.addAll(getRouteClient().getRouteGroups());
            }
            allRouteGroups.stream()
                .filter(
                    group -> StringUtils.equalsIgnoreCase(routeGroup.getName(), group.getName()))
                .findFirst()
                .ifPresent(group -> getRouteClient().deleteRouteGroup(group.getId()));
          }
        } catch (Throwable ex) {
          LOGGER.warn("Could not delete Route Group " + routeGroup.getName(), ex);
        }
      });
    }
  }

  @After("@DeleteCreatedSalesPerson")
  public void deleteSalesPerson() {
    final List<SalesPerson> salesPersons = get(KEY_CORE_LIST_OF_SALES_PERSON);
    if (Objects.isNull(salesPersons) || salesPersons.isEmpty()) {
      LOGGER.trace(
          "no sales person has been created under key \"KEY_LIST_OF_SALES_PERSON\", skip the delete sales person");
      return;
    }
    salesPersons.forEach(o -> {
      try {
        doWithRetry(() -> {
          getSalesClient().deleteSalesPerson(o.getId());
          LOGGER.debug("Sales Person ID = {} delete successfully", o.getId());
        }, "After hook: @DeleteCreatedSalesPerson");
      } catch (Throwable t) {
        LOGGER.warn("Error to delete sales person: {}", t.getMessage());
      }
    });
  }

  @After("@DeleteOrderTagsV2")
  public void deleteOrderTagsV2() {
    final List<OrderTag> tags = get(KEY_CORE_LIST_OF_CREATED_ORDER_TAGS);
    if (CollectionUtils.isNotEmpty(tags)) {
      tags.forEach(orderTag -> {
        if (orderTag != null) {
          try {
            if (orderTag.getId() != null) {
              getOrderClient().deleteOrderTag(orderTag.getId());
            } else {
              getOrderClient().deleteOrderTag(orderTag.getName());
            }
          } catch (Throwable ex) {
            LOGGER.warn("Could not delete order tag [{}]", orderTag.getName(), ex);
          }
        }
      });
    }
  }

  @After("@DeleteRouteTagsV2")
  public void deleteRouteTagsV2() {
    final List<RouteTag> tags = get(KEY_CORE_LIST_OF_CREATED_ROUTE_TAGS);
    if (CollectionUtils.isNotEmpty(tags)) {
      tags.forEach(tag -> {
        if (tag != null) {
          try {
            if (tag.getId() != null) {
              getTagClient().deleteTag(tag.getId());
            } else {
              getTagClient().deleteTag(tag.getName());
            }
          } catch (Throwable ex) {
            LOGGER.warn("Could not delete route tag [{}]", tag.getName(), ex);
          }
        }
      });
    }
  }

  @After("@RestoreSmsNotificationsSettingsV2")
  public void restoreSmsNotifSettings() {
    SmsNotificationsSettings settings = get(KEY_CORE_SMS_NOTIFICATIONS_SETTINGS);
    try {
      if (settings != null) {
        doWithRetry(() ->
                getNotificationsClient().updateSmsNotificationsSettings(settings),
            "update sms notification settings");
      }
    } catch (Throwable ex) {
      LOGGER.warn("could not restore sms notification message {}", settings);
    }
  }

  @After("@DeletePrinterV2")
  public void deletePrinter() {
    PrinterSettings printerSettings = get(KEY_CORE_PRINTER_SETTINGS);
    if (printerSettings != null) {
      try {
        if (printerSettings.getId() == null) {
          List<PrinterSettings> allPrinters = getPrintersClient().getAll();
          allPrinters.stream()
              .filter(printer -> StringUtils
                  .equalsIgnoreCase(printerSettings.getName(), printer.getName()))
              .findFirst()
              .ifPresent(doWithRetry(() -> printer -> getPrintersClient().delete(printer.getId()),
                  "printer deleted"));
        } else {
          doWithRetry(() -> getPrintersClient().delete(printerSettings.getId()), "printer deleted");
        }
      } catch (Throwable ex) {
        LOGGER.warn(f("Could not delete printer [%s]", printerSettings.getName()), ex);
      }
    }
  }

  @After("@DeleteThirdPartyShippersV2")
  public void deleteThirdPartyShippers() {
    ThirdPartyShippers thirdPartyShipper =
        containsKey(KEY_CORE_CREATED_THIRD_PARTY_SHIPPER_EDITED) ? get(
            KEY_CORE_CREATED_THIRD_PARTY_SHIPPER_EDITED)
            : get(KEY_CORE_CREATED_THIRD_PARTY_SHIPPER);
    if (thirdPartyShipper != null) {
      if (thirdPartyShipper.getId() != null) {
        try {
          doWithRetry(() -> getThirdPartyShippersClient().delete(thirdPartyShipper.getId()),
              "delete third part shipper");
        } catch (Throwable ex) {
          LOGGER.warn(f("Could not delete Third Party Shipper [%s]", ex.getMessage()));
        }
      } else {
        LOGGER.warn(f("Could not delete Third Party Shipper [%s] - id was not defined",
            thirdPartyShipper.getName()));
      }
    }
  }
}
