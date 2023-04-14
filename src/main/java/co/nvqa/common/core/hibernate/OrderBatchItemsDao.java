package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OrderBatchItems;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class OrderBatchItemsDao extends DbBase {

  public OrderBatchItemsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public OrderBatchItems getBatchId(Long batchId) {
    String query = "FROM OrderBatchItems WHERE batchId =:batchId";
    var result = findAll(session ->
        session.createQuery(query, OrderBatchItems.class)
            .setParameter("batchId", batchId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
