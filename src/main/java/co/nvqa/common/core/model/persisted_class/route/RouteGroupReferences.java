package co.nvqa.common.core.model.persisted_class.route;

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

  public RouteGroupReferences() {
  }

  public RouteGroupReferences(Map<String, ?> data) {
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

  public Long getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(Long referenceId) {
    this.referenceId = referenceId;
  }

  public Long getReferenceTypeId() {
    return referenceTypeId;
  }

  public void setReferenceTypeId(Long referenceTypeId) {
    this.referenceTypeId = referenceTypeId;
  }

  public Long getRouteGroupId() {
    return routeGroupId;
  }

  public void setRouteGroupId(Long routeGroupId) {
    this.routeGroupId = routeGroupId;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }
}
