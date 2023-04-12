package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.ReservationFailureReason;
import co.nvqa.common.core.model.persisted_class.core.TransactionFailureReasons;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;

@Singleton
public class TransactionReservationFailureReasonDao extends DbBase {

  public TransactionReservationFailureReasonDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public TransactionFailureReasons findTransactionFailureReasonByTransactionId(Long transactionId) {
    String query = "FROM TransactionFailureReasons WHERE transactionId = :transactionId";
    return findOne(session -> session.createQuery(query, TransactionFailureReasons.class)
        .setParameter("transactionId", transactionId));
  }

  public ReservationFailureReason findReservationFailureReasonByRsvnId(Long reservationId) {
    String query = "FROM ReservationFailureReason WHERE reservationId = :reservationId";
    return findOne(session -> session.createQuery(query, ReservationFailureReason.class)
        .setParameter("reservationId", reservationId));
  }

}
