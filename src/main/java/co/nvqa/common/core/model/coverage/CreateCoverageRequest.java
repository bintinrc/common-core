package co.nvqa.common.core.model.coverage;

import co.nvqa.common.model.DataEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCoverageRequest extends DataEntity<CreateCoverageRequest> {

  private String area;
  private List<String> areaVariations = new ArrayList<>();
  private Long fallbackDriverId;
  private Long hubId;
  private List<String> keywords = new ArrayList<>();
  private Long primaryDriverId;

  public CreateCoverageRequest(Map<String, ?> data) {
    fromMap(data);
  }

  public void setAreaVariations(List<String> areaVariations) {
    this.areaVariations = areaVariations;
  }

  public void setAreaVariations(String areaVariations) {
    setAreaVariations(splitAndNormalize(areaVariations));
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public void setKeywords(String keywords) {
    setKeywords(splitAndNormalize(keywords));
  }
}
