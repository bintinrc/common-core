package co.nvqa.common.core.model.pickup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MilkRunGroup {

  private Long id;
  private Long driverId;
  private Long hubId;
  private String name;
  private List<Long> samsIds;
  private String status;

  public Long getId() {
    return id;
  }

  public MilkRunGroup setId(Long id) {
    this.id = id;
    return this;
  }

  public MilkRunGroup setDriverID(Long driverId) {
    this.driverId = driverId;
    return this;
  }

  public MilkRunGroup setHubId(Long hubId) {
    this.hubId = hubId;
    return this;
  }

  public MilkRunGroup setName(String name) {
    this.name = name;
    return this;
  }

  public MilkRunGroup setSamsIds(List<Long> samsIds) {
    this.samsIds = samsIds;
    return this;
  }

}
