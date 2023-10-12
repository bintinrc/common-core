package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OrderDetails;
import co.nvqa.common.core.model.persisted_class.core.Orders;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.NvTestRuntimeException;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

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

  public String getOrdersServiceLevel(Long orderId) {
    String query = "FROM OrderDetails WHERE orderId = :orderId";
    var result = findAll(session ->
        session.createQuery(query, OrderDetails.class)
            .setParameter("orderId", orderId));
    OrderDetails order = CollectionUtils.isEmpty(result) ? null : result.get(0);
    if (order != null) {
      return order.getServiceLevel();
    } else {
      throw new NvTestRuntimeException("order record is not found for order " + orderId);
    }
  }
}
