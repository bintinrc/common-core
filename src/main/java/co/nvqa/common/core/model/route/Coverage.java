package co.nvqa.common.core.model.route;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
import java.util.Map;

public class Coverage extends DataEntity<Coverage> {

  private Long id;
  private String area;
  private Long hubId;
  private Long primaryDriverId;
  private Long fallbackDriverId;
  private Date createdAt;
  private Date updatedAt;
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
}
