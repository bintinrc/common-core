package co.nvqa.common.core.model.route;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 5/18/17.
 *
 * @author felixsoewito
 * <p>
 * JSON format: camel case
 */
@Setter
@Getter
public class RouteRequest {

  private String date;
  private String dateTime;
  private Long driverId;
  private Long hubId;
  private String comments;
  private List<Integer> tags = new ArrayList<>();
  private Long zoneId;
}
