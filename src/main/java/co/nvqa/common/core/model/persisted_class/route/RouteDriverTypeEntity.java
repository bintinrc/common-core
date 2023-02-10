package co.nvqa.common.core.model.persisted_class.route;

import java.util.Date;

/**
 * @author Daniel Joi Partogi Hutapea
 */
public class RouteDriverTypeEntity {

  private Long id;
  private String country;
  private String systemId;
  private Long routeId;
  private Long driverTypeId;
  private Date createdAt;
  private Date updatedAt;
  private Date deletedAt;

  public RouteDriverTypeEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  public Long getRouteId() {
    return routeId;
  }

  public void setRouteId(Long routeId) {
    this.routeId = routeId;
  }

  public Long getDriverTypeId() {
    return driverTypeId;
  }

  public void setDriverTypeId(Long driverTypeId) {
    this.driverTypeId = driverTypeId;
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
