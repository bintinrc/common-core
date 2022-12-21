package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.OrderPickup;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class OrderPickupsDao extends DbBase {

  public OrderPickupsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class");
  }

  public List<OrderPickup> getOrderPickupByOrderId(Long orderId) {
    List<OrderPickup> results;
    String query = " FROM OrderPickup "
        + "WHERE orderId = :orderId "
        + "AND reservationId IS NOT NULL "
        + "AND deletedAt IS NULL";
    results = findAll(session ->
        session.createQuery(query, OrderPickup.class)
            .setParameter("orderId", orderId));
    return results;
  }
}
