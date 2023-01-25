package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.Reservations;
import co.nvqa.common.core.model.persisted_class.Waypoints;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class ReservationsDao extends DbBase {

  public ReservationsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class");
  }

  public List <Reservations> getReservationsDetailsByReservationId(Long reservationId) {
    List<Reservations> results;
    String query = "FROM Reservations "
        + "WHERE id = :reservationId";
    results = findAll(session ->
        session.createQuery(query, Reservations.class)
            .setParameter("reservationId", reservationId));
    return results;
  }
}