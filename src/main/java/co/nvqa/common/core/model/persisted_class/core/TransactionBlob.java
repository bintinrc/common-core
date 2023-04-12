package co.nvqa.common.core.model.persisted_class.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_blob")
public class TransactionBlob {

  @Id
  private Long id;
  @Column(name = "transaction_id")
  private Long transactionId;
  @Column(name = "blob_id")
  private Long blobId;
  @Column(name = "deleted_at")
  private String deletedAt;

}
