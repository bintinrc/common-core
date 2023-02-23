package co.nvqa.common.core.model.persisted_class.route;

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
@Table(name = "route_groups")
public class RouteGroup extends DataEntity<RouteGroup> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "updated_at")
  private String updatedAt;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "system_id")
  private String systemId;

  public RouteGroup(Map<String, ?> data) {
    fromMap(data);
  }

}