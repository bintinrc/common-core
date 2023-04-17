package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.CodInbounds;
import co.nvqa.common.core.model.persisted_class.core.TransactionBlob;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class CodInboundDao extends DbBase {


  public CodInboundDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public CodInbounds getCodInbound(Long routeId) {
    String query = "FROM CodInbounds WHERE routeId =:routeId AND deletedAt is NULL";
    var result = findAll(session ->
        session.createQuery(query, CodInbounds.class)
            .setParameter("routeId", routeId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
