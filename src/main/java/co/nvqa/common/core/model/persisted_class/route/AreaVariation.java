package co.nvqa.common.core.model.persisted_class.route;

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
@Table(name = "sr_area_variations_v2")
public class AreaVariation extends DataEntity<AreaVariation> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "area")
  private String area;
  @Column(name = "variation_name")
  private String variationName;
  @Column(name = "system_id")
  private String systemId;
  @Column(name = "hub_id")
  private Long hubId;

  public AreaVariation(Map<String, ?> data) {
    super(data);
  }

}