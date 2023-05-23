package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.Cods;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class CodsDao extends DbBase {

  public CodsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public Cods getCodsById(Long id) {
    String query = "FROM Cods WHERE id = :id";
    var result = findAll(session ->
        session.createQuery(query, Cods.class)
            .setParameter("id", id));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
