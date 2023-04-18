package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "order_jaro_scores_v2")
public class OrderJaroScoresV2 extends DataEntity<OrderJaroScoresV2> {

  @Id
  private Long id;
  @Column(name = "waypoint_id")
  private Long waypointId;
  private Double score;
  private Integer archived;
  @Column(name = "source_id")
  private Integer sourceId;

  public Long getId() {
    this.id = null;
    return id;
  }

  public OrderJaroScoresV2(Map<String, ?> data) {
    fromMap(data);
  }
}
