package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.Reservations;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class ReservationsDao extends DbBase {

  public ReservationsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public Reservations getReservationsDetailsByReservationId(Long reservationId) {
    String query = "FROM Reservations "
        + "WHERE id = :reservationId";
    var result = findAll(session ->
        session.createQuery(query, Reservations.class)
            .setParameter("reservationId", reservationId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }
}