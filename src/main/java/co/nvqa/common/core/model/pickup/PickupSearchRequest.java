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
  private String fromDatetime;
  private String toDatetime;
  private List<Integer> types; //e.g.: "0 = Normal", "1 = On-Demand", "2 = Hyperlocal"
  private List<String> waypointStatus; //e.g.: "Pending", "Routed", "Fail", "Success"
  private List<Long> hubIds;
  private List<Long> shipperIds; //Legacy shipper ID
  private Integer maxResult;
  private Integer lastId;

}
