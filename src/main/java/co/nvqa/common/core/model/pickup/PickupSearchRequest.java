package co.nvqa.common.core.model.pickup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PickupSearchRequest {

  private List<Long> reservationIds;

}
