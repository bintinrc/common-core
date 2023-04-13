package co.nvqa.common.core.model.persisted_class.core;


import co.nvqa.common.model.DataEntity;
import java.util.Map;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cod_collections")
public class CodCollections extends DataEntity<CodCollections> {

  @Id
  private Long id;
  @Column(name = "cod_id")
  private Long codId;
  @Column(name = "collected_sum")
  private Double collectedSum;
  @Column(name = "driver_id")
  private Long driverId;
  @Column(name = "waypoint_id")
  private Long waypointId;
  @Column(name = "audit_log")
  private String auditLog;

  public CodCollections(Map<String, ?> data) {
    super(data);
  }

}
