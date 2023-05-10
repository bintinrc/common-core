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

/**
 * @author Sergey Mishanin
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "route_logs")
public class CoreRouteLogs extends DataEntity<CoreRouteLogs> {

  @Id
  private Long id;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "archived")
  private Integer archived;
  @Column(name = "status")
  private Integer status;
  @Column(name = "is_ok")
  private Integer isOk;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "zone_id")
  private Long zoneId;
  @Column(name = "driver_id")
  private Long driverId;

  public CoreRouteLogs(Map<String, ?> data) {
    super(data);
  }

}