package co.nvqa.common.core.model.route;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditRouteRequest {

  private Long id;
  private String date;
  private String datetime;
  private Long driverId;
  private Long hubId;
  private List<Integer> tags = new ArrayList<>();
  private Long zoneId;
}
