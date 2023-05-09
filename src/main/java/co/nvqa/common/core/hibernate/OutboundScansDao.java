package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OutboundScans;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class OutboundScansDao extends DbBase {

  public OutboundScansDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public OutboundScans getOutboundScansByOrderId(Long orderId) {
    String query = "FROM OutboundScans WHERE orderId = :orderId";
    var result = findAll(session ->
        session.createQuery(query, OutboundScans.class)
            .setParameter("orderId", orderId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
