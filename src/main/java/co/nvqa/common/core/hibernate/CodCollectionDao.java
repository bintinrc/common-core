package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.CodCollections;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class CodCollectionDao extends DbBase {

  public CodCollectionDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public CodCollections getSingleCodCollection(Long waypointId) {
    String query = "FROM CodCollections WHERE waypointId = :waypointId";
    var result = findAll(session ->
        session.createQuery(query, CodCollections.class)
            .setParameter("waypointId", waypointId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public List<CodCollections> getMultipleCodCollections(Long waypointId) {
    String query = "FROM CodCollections WHERE waypointId = :waypointId";
    return findAll(session ->
        session.createQuery(query, CodCollections.class)
            .setParameter("waypointId", waypointId));
  }

}
