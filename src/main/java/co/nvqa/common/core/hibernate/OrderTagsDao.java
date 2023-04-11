package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OrderTags;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class OrderTagsDao extends DbBase {

  public OrderTagsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public List<OrderTags> getMultipleTransactions(Long orderId) {
    String query = "FROM OrderTags WHERE orderId = :orderId";
    return findAll(session ->
        session.createQuery(query, OrderTags.class)
            .setParameter("orderId", orderId));
  }
}
