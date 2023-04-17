package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.OrderJaroScoresV2;
import co.nvqa.common.core.model.persisted_class.core.Transactions;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class OrderJaroScoresV2Dao extends DbBase {

  public OrderJaroScoresV2Dao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public OrderJaroScoresV2 getSingleOjs(Long waypointId) {
    String query = "FROM OrderJaroScoresV2 WHERE waypointId = :waypointId";
    var result = findAll(session ->
        session.createQuery(query, OrderJaroScoresV2.class)
            .setParameter("waypointId", waypointId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public List<OrderJaroScoresV2> getMultipleOjs(Long waypointId) {
    String query = "FROM OrderJaroScoresV2 WHERE waypointId = :waypointId";
    return findAll(session ->
        session.createQuery(query, OrderJaroScoresV2.class)
            .setParameter("waypointId", waypointId));
  }

  public void unarchiveJaroScores(Long waypointId) {
    String query = "UPDATE OrderJaroScoresV2 SET archived = 0" + " WHERE waypointId =" + waypointId;
    saveOrUpdate(s -> s.createQuery(query));
  }
}
