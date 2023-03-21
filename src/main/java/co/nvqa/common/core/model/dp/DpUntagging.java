package co.nvqa.common.core.model.dp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DpUntagging {

  private DpUntag dpUntag;
  private RemoveFromRouteDp removeFromRouteDp;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DpUntag {

    private Long userId;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RemoveFromRouteDp {

    private String type;
  }

}
