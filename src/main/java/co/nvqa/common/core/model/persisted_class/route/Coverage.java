package co.nvqa.common.core.model.persisted_class.route;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
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
@Table(name = "sr_coverages")
public class Coverage extends DataEntity<Coverage> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "area")
  private String area;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "primary_driver_id")
  private Long primaryDriverId;
  @Column(name = "fallback_driver_id")
  private Long fallbackDriverId;
  @Column(name = "system_id")
  private String systemId;
  @Column(name = "created_at")
  private Date createdAt;
  @Column(name = "updated_at")
  private Date updatedAt;
  @Column(name = "deleted_at")
  private Date deletedAt;

  public Coverage(Map<String, ?> data) {
    super(data);
  }

}