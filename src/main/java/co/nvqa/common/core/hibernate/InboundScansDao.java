package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.InboundScans;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class InboundScansDao extends DbBase {

  public InboundScansDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public List<InboundScans> findInboundScansByOrderId(Long orderId) {
    String query = "FROM InboundScans WHERE orderId = :orderId";

    return findAll(session ->
        session.createQuery(query, InboundScans.class)
            .setParameter("orderId", orderId));
  }

}
