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
@Table(name = "reservation_blob")
public class ReservationBlob {

  @Id
  private Long id;
  @Column(name = "reservation_id")
  private Long reservationId;
  @Column(name = "blob_id")
  private Long blobId;
  @Column(name = "deleted_at")
  private String deletedAt;

}
