package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.routing_search.Transactions;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;

public class RoutingSearchDao extends DbBase {

  public RoutingSearchDao() {
    super(CoreTestConstants.DB_ROUTING_SEARCH_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.routing_search");
  }


  public Transactions getTransaction(Long txnId) {
    String query = "FROM Transactions WHERE systemId = :systemId AND txnId = :txnId ORDER BY id DESC";

    return findOne(session ->
        session.createQuery(query, Transactions.class)
            .setParameter("systemId", StandardTestConstants.NV_SYSTEM_ID)
            .setParameter("txnId", txnId));
  }
}
