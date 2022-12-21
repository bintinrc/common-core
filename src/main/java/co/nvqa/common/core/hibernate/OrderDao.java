package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.Order;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.NvTestRuntimeException;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class OrderDao extends DbBase {

  public OrderDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class");
  }

  public Double getOrderWeight(Long orderId) {
    Double result;
    String query = "FROM Order WHERE id = :orderId AND deletedAt IS NULL";
    List<Order> orders = findAll(session ->
        session.createQuery(query, Order.class)
            .setParameter("orderId", orderId));
    if (orders != null && !orders.isEmpty()) {
      result = orders.get(0).getWeight();
      return result;
    } else {
      throw new NvTestRuntimeException("order record is not found for order " + orderId);
    }
  }

}
