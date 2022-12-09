package co.nvqa.common.core.cucumber.glue;

import co.nvqa.common.core.cucumber.CoreStandardSteps;
import co.nvqa.common.core.hibernate.PickupAppointmentJobsOrdersDao;
import co.nvqa.common.core.model.persisted_class.PickupAppointmentJobsOrders;
import co.nvqa.common.utils.NvTestRuntimeException;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.stream.Collectors;

public class DbPickupAppointmentJobsOrdersSteps extends CoreStandardSteps {

  private PickupAppointmentJobsOrdersDao pickupAppointmentJobsOrdersDao;

  @Override
  public void init() {
    pickupAppointmentJobsOrdersDao = new PickupAppointmentJobsOrdersDao();
  }

  /**
   * This method enforce the pickup appointment to be non NULL in order_pickups table
   *
   * @param orderIdString KEY contains order id
   */
  @When("DB Core - get pickup appointment job id from order id {string}")
  public void getReservationIdFromOrderId(String orderIdString) {
    final Long orderId = Long.parseLong(resolveValue(orderIdString));
    final List<PickupAppointmentJobsOrders> pickupAppointmentJobsOrders = retryIfAssertionErrorOrRuntimeExceptionOccurred(() -> {
      final List<PickupAppointmentJobsOrders> result = pickupAppointmentJobsOrdersDao.getPickupAppointmentJobsOrdersByOrderId(orderId);
      if (result.isEmpty()) {
        throw new NvTestRuntimeException("pickup appointment job is not found for order " + orderId);
      }
      return result;
    }, "reading pickup appointment job from order id: " + orderId);
    put(KEY_LIST_OF_PICKUP_APPOINTMENT_IDS,
        pickupAppointmentJobsOrders.stream()
            .map(PickupAppointmentJobsOrders::getPickupAppointmentJobId)
            .collect(Collectors.toList()));
  }
}
