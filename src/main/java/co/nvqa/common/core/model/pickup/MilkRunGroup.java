package co.nvqa.common.core.model.pickup;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MilkRunGroup {

  private Long id;

  @JsonProperty("driver_id")
  private Long driverId;

  @JsonProperty("hub_id")
  private Long hubId;

  private String name;

  @JsonProperty("sams_ids")
  private List<Long> samsIds;

  private String status;

}
