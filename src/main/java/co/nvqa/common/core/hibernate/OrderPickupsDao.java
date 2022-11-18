package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.OrderPickup;
import co.nvqa.common.database.DbBase;
import java.util.List;

public class OrderPickupsDao extends DbBase {

  public OrderPickupsDao(String url, String dbUser, String password, String modelPath) {
    super(url, dbUser, password, modelPath);
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
