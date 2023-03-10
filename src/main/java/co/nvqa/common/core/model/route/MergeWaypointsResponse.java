package co.nvqa.common.core.model.route;

import co.nvqa.common.model.DataEntity;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MergeWaypointsResponse extends DataEntity<MergeWaypointsResponse> {

  private List<Data> data;

  @Getter
  @Setter
  public static class Data {

    private Data data;
    private List<Long> transactionIds;
    private Long waypointId;
  }

  public MergeWaypointsResponse(Map<String, ?> expectedData) {
    super(expectedData);
  }

}
