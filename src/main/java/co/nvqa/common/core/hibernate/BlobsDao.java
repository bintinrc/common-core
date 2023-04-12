package co.nvqa.common.core.hibernate;

import co.nvqa.common.core.model.persisted_class.core.Blobs;
import co.nvqa.common.core.model.persisted_class.core.ReservationBlob;
import co.nvqa.common.core.model.persisted_class.core.TransactionBlob;
import co.nvqa.common.core.utils.CoreTestConstants;
import co.nvqa.common.database.DbBase;
import co.nvqa.common.utils.StandardTestConstants;
import javax.inject.Singleton;
import org.apache.commons.collections.CollectionUtils;

@Singleton
public class BlobsDao extends DbBase {

  public BlobsDao() {
    super(CoreTestConstants.DB_CORE_URL, StandardTestConstants.DB_USER,
        StandardTestConstants.DB_PASS, "co.nvqa.common.core.model.persisted_class.core");
  }

  public TransactionBlob getTransactionBlob(Long transactionId) {
    String query = "FROM TransactionBlob WHERE transactionId =:transactionId AND deletedAt is NULL";
    var result = findAll(session ->
        session.createQuery(query, TransactionBlob.class)
            .setParameter("transactionId", transactionId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public ReservationBlob getReservationBlob(Long reservationId) {
    String query = "FROM ReservationBlob WHERE reservationId =:reservationId AND deletedAt is NULL";
    var result = findAll(session ->
        session.createQuery(query, ReservationBlob.class)
            .setParameter("reservationId", reservationId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public Blobs getBlobById(Long blobId) {
    String query = "FROM Blobs WHERE id =:blobId AND deletedAt is NULL";
    var result = findAll(session ->
        session.createQuery(query, Blobs.class)
            .setParameter("blobId", blobId));
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

}
