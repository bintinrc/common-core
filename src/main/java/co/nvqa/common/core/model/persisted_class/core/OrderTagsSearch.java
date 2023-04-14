package co.nvqa.common.core.model.persisted_class.core;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_tags_search")
public class OrderTagsSearch extends DataEntity<OrderTagsSearch> {

  @Id
  private Long id;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "tracking_id")
  private String trackingId;
  @Column(name = "parcel_granular_status")
  private String parcelGranularStatus;
  @Column(name = "order_tag_ids")
  @ElementCollection
  private Set<Long> orderTagIds;
  @Column(name = "route_id")
  private Long routeId;
  @Column(name = "driver_id")
  private Long driverId;

  public OrderTagsSearch(Map<String, ?> data) {
    super(data);
  }

  public void setOrderTagIds(Set<Long> orderTagIds) {
    this.orderTagIds = orderTagIds;
  }

  public void setOrderTagIds(String orderTagIds) {
    setOrderTagIds(splitAndNormalize(orderTagIds).stream()
        .map(Long::valueOf)
        .collect(Collectors.toSet())
    );
  }
}
