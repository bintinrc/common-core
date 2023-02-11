package co.nvqa.common.core.model.persisted_class.route;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sergey Mishanin
 */
@Entity
@Table(name = "sr_coverages")
public class Coverage extends DataEntity<Coverage> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "area")
  private String area;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "primary_driver_id")
  private Long primaryDriverId;
  @Column(name = "fallback_driver_id")
  private Long fallbackDriverId;
  @Column(name = "system_id")
  private String systemId;
  @Column(name = "created_at")
  private Date createdAt;
  @Column(name = "updated_at")
  private Date updatedAt;
  @Column(name = "deleted_at")
  private Date deletedAt;

  public Coverage() {
  }

  public Coverage(Map<String, ?> data) {
    super(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public Long getHubId() {
    return hubId;
  }

  public void setHubId(Long hubId) {
    this.hubId = hubId;
  }

  public Long getPrimaryDriverId() {
    return primaryDriverId;
  }

  public void setPrimaryDriverId(Long primaryDriverId) {
    this.primaryDriverId = primaryDriverId;
  }

  public Long getFallbackDriverId() {
    return fallbackDriverId;
  }

  public void setFallbackDriverId(Long fallbackDriverId) {
    this.fallbackDriverId = fallbackDriverId;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Date getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Date deletedAt) {
    this.deletedAt = deletedAt;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }
}
