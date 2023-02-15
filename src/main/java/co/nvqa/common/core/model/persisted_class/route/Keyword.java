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
@Table(name = "sr_keywords")
public class Keyword extends DataEntity<Keyword> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "coverage_id")
  private Long coverageId;
  @Column(name = "value")
  private String value;

  public Keyword(Map<String, ?> data) {
    super(data);
  }

}