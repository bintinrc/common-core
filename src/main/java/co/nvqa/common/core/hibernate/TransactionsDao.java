package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.Transactions;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class TransactionsDao extends DbBase {

  public TransactionsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public Transactions getSingleTransaction(Long transactionId) {
    String query = "FROM Transactions WHERE id = :transactionId";
    var result = findAll(session ->
        session.createQuery(query, Transactions.class)
            .setParameter("transactionId", transactionId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }
}
