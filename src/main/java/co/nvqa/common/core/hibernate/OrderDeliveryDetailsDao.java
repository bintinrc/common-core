package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OrderDeliveryDetails;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class OrderDeliveryDetailsDao extends DbBase {

  public OrderDeliveryDetailsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public OrderDeliveryDetails getOrderDeliveryDetails(Long orderId) {
    String query = "FROM OrderDeliveryDetails WHERE orderId =:orderId";
    var result = findAll(session ->
        session.createQuery(query, OrderDeliveryDetails.class)
            .setParameter("orderId", orderId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
