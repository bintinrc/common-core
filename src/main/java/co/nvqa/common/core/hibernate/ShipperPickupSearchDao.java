package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.core.ShipperPickupSearch;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class ShipperPickupSearchDao extends DbBase {

  public ShipperPickupSearchDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.core");
  }

  public ShipperPickupSearch getShipperPickupSearchByReservationId(Long reservationId) {
    String query = "FROM ShipperPickupSearch WHERE reservationId = :reservationId";
    List<ShipperPickupSearch> result = findAll(session ->
        session.createQuery(query, ShipperPickupSearch.class)
            .setParameter("reservationId", reservationId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
