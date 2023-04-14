package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OrderDetails;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class OrderDetailsDao extends DbBase {

  public OrderDetailsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public OrderDetails getOrderDetailsByOrderId(Long orderId) {
    String query = "FROM OrderDetails WHERE orderId = :orderId";
    return findOne(session -> session.createQuery(query, OrderDetails.class)
        .setParameter("orderId", orderId));
  }

  public List<OrderDetails> getMultipleOrderDetailsByOrderId(Long orderId) {
    String query = "FROM OrderDetails WHERE orderId = :orderId";
    return findAll(session -> session.createQuery(query, OrderDetails.class)
        .setParameter("orderId", orderId));
  }
}
