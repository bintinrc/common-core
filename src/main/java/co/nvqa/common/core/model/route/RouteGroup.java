package co.nvqa.common.core.model.route;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sergey Mishanin
 */
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

  public RouteGroup() {
  }

  public RouteGroup(Map<String, ?> data) {
    fromMap(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(String deletedAt) {
    this.deletedAt = deletedAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }
}
