package co.nvqa.common.core.model;

import co.nvqa.common.model.DataEntity;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteGroup extends DataEntity<RouteGroup> {

  private String createdAt;
  private String updatedAt;
  private String description;
  private Long id;
  private String name;
  private List<Long> transactionIds;
  private List<Long> reservationIds;
  private List<Long> pickupAppointmentJobIds;

  public RouteGroup(Map<String, ?> data) {
    super(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setId(String id) {
    setId(Long.parseLong(id));
  }
}
