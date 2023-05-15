package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OrderTagsSearch;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class OrderTagsSearchDao extends DbBase {

  public OrderTagsSearchDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public List<OrderTagsSearch> getOrderTagsSearch(Long orderId) {
    String query = "FROM OrderTagsSearch WHERE orderId = :orderId";
    return findAll(session -> session.createQuery(query, OrderTagsSearch.class)
        .setParameter("orderId", orderId));
  }

}
