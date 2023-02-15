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
@Table(name = "route_group_references")
public class RouteGroupReferences extends DataEntity<RouteGroupReferences> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "updated_at")
  private String updatedAt;
  @Column(name = "reference_id")
  private Long referenceId;
  @Column(name = "reference_type_id")
  private Long referenceTypeId;
  @Column(name = "route_group_id")
  private Long routeGroupId;
  @Column(name = "system_id")
  private String systemId;

  public RouteGroupReferences(Map<String, ?> data) {
    fromMap(data);
  }

}