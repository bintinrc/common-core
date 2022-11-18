package co.nvqa.common.core.model.reservation;

import co.nvqa.common.utils.JsonUtils;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created on 18/04/18.
 *
 * @author Felix Soewito
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationFilter {

  private Long addressId;
  private Long dpId;
  private String endLatestdate;
  private String endReadydate;
  private Long reservationId;
  private Long shipperId;
  private String startLatestdate;
  private String startReadydate;

  public Map<String, String> toQueryParam() {
    final Map<String, Object> map = JsonUtils.convertValueToMap(
        JsonUtils.getDefaultSnakeCaseMapper(), this, String.class, Object.class);
    return map.entrySet().stream()
        .filter(e -> e.getValue() != null)
        .map(e -> Map.entry(e.getKey(), String.valueOf(e.getValue())))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

}
