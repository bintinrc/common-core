package co.nvqa.common.core.model.persisted_class.route;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sergey Mishanin
 */
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

  public Keyword() {
  }

  public Keyword(Map<String, ?> data) {
    super(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCoverageId() {
    return coverageId;
  }

  public void setCoverageId(Long coverageId) {
    this.coverageId = coverageId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
