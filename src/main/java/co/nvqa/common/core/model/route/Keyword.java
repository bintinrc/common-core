package co.nvqa.common.core.model.route;

import co.nvqa.common.model.DataEntity;
import java.util.Map;

public class Keyword extends DataEntity<Keyword> {

  private Long id;
  private Long coverageId;
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
