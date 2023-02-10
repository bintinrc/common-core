package co.nvqa.common.core.model.persisted_class.core;

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
@Table(name = "route_logs")
public class RouteLogs extends DataEntity<RouteLogs> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "deleted_at")
  private String deletedAt;
  @Column(name = "archived")
  private Integer archived;
  @Column(name = "status")
  private Integer status;
  @Column(name = "is_ok")
  private Integer isOk;
  @Column(name = "hub_id")
  private Long hubId;
  @Column(name = "zone_id")
  private Long zoneId;

  public RouteLogs() {
  }

  public RouteLogs(Map<String, ?> data) {
    super(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(String deletedAt) {
    this.deletedAt = deletedAt;
  }

  public int getArchived() {
    return archived;
  }

  public void setArchived(int archived) {
    this.archived = archived;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Long getHubId() {
    return hubId;
  }

  public void setHubId(Long hubId) {
    this.hubId = hubId;
  }

  public Long getZoneId() {
    return zoneId;
  }

  public void setZoneId(Long zoneId) {
    this.zoneId = zoneId;
  }

  public int getIsOk() {
    return isOk;
  }

  public void setIsOk(int isOk) {
    this.isOk = isOk;
  }
}
