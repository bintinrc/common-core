package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.InboundScans;
import co.nvqa.common.core.model.persisted_class.core.WarehouseSweeps;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class WarehouseSweepsDao extends DbBase {

  public WarehouseSweepsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public List<WarehouseSweeps> findWarehouseSweepRecord(String trackingId) {
    String query = "FROM WarehouseSweeps WHERE scan = :trackingId";
    return findAll(session ->
        session.createQuery(query, WarehouseSweeps.class)
            .setParameter("trackingId", trackingId));
  }
}
