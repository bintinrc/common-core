package co.nvqa.common.core.model.persisted_class.events;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sergey Mishanin
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "order_event_types")
public class OrderEventTypes extends DataEntity<OrderEventTypes> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "name")
  private String name;
  @Column(name = "updated_at")
  private String updatedAt;

  public OrderEventTypes(Map<String, ?> data) {
    fromMap(data);
  }

}