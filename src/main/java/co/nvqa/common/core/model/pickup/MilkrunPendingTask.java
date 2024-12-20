package co.nvqa.common.core.model.pickup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MilkrunPendingTask {

  Long id;

  @JsonProperty("sams_id")
  Long samsId;

  @JsonProperty("shipper_id")
  Long shipperId;

  Boolean link;
}
