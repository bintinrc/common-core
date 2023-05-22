package co.nvqa.common.core.model.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkAddPickupJobToRouteRequest {

  @JsonProperty("ids")
  private List<Long> paJobIds;
  private String newRouteId;
  private Boolean overwrite;
}
