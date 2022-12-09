package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.PickupAppointmentJobsOrders;
import co.nvqa.common.core.utils.ControlTestConstans;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;

public class PickupAppointmentJobsOrdersDao extends DbBase {

  public PickupAppointmentJobsOrdersDao() {
    super(ControlTestConstans.DB_CONTROL_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class");
  }

  public List<PickupAppointmentJobsOrders> getPickupAppointmentJobsOrdersByOrderId(Long orderId) {
    List<PickupAppointmentJobsOrders> results;
    String query = " FROM PickupAppointmentJobsOrders "
        + "WHERE orderId = :orderId "
        + "AND deletedAt IS NULL";
    results = findAll(session ->
        session.createQuery(query, PickupAppointmentJobsOrders.class)
            .setParameter("orderId", orderId));
    return results;
  }
}
