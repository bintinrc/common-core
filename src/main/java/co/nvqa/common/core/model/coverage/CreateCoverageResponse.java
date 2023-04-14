package co.nvqa.common.core.model.coverage;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCoverageResponse {

  private Data data;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Data {

    private Long id;
    private Long hubId;
    private String area;
    private Long primaryDriverId;
    private Long fallbackDriverId;
    private List<Keyword> keywords;
    private List<String> variationNames;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Keyword {

      private Long id;
      private Long coverageId;
      private String value;
    }

  }
}
