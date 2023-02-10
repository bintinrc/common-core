package co.nvqa.common.core.model.persisted_class.route;

import co.nvqa.common.model.DataEntity;
import java.util.Map;

public class AreaVariation extends DataEntity<AreaVariation> {

  private Long id;
  private String area;
  private String variationName;

  public AreaVariation() {
  }

  public AreaVariation(Map<String, ?> data) {
    super(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getVariationName() {
    return variationName;
  }

  public void setVariationName(String variationName) {
    this.variationName = variationName;
  }
}
