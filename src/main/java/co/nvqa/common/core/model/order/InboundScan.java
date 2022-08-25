package co.nvqa.common.core.model.order;

import java.io.Serializable;

/**
 * Created on 11/17/17.
 *
 * @author felixsoewito
 * <p>
 * JSON format: camel case
 */
public class InboundScan implements Serializable {

  private String createdAt;
  private Long hubId;
  private String location;
  private String result;
  private Long routeId;
  private String scannedBy;
  private String type;
  private Object scan;
  private Object deletedAt;
  private Integer id;
  private Object activeOrder;
  private Object updatedAt;
  private Object activeRoute;

  public String getCreatedAt() {
    return createdAt;
  }

  public InboundScan setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Long getHubId() {
    return hubId;
  }

  public InboundScan setHubId(Long hubId) {
    this.hubId = hubId;
    return this;
  }

  public String getLocation() {
    return location;
  }

  public InboundScan setLocation(String location) {
    this.location = location;
    return this;
  }

  public String getResult() {
    return result;
  }

  public InboundScan setResult(String result) {
    this.result = result;
    return this;
  }

  public Long getRouteId() {
    return routeId;
  }

  public InboundScan setRouteId(Long routeId) {
    this.routeId = routeId;
    return this;
  }

  public String getScannedBy() {
    return scannedBy;
  }

  public InboundScan setScannedBy(String scannedBy) {
    this.scannedBy = scannedBy;
    return this;
  }

  public String getType() {
    return type;
  }

  public InboundScan setType(String type) {
    this.type = type;
    return this;
  }

  public Object getScan() {
    return scan;
  }

  public InboundScan setScan(Object scan) {
    this.scan = scan;
    return this;
  }

  public Object getDeletedAt() {
    return deletedAt;
  }

  public InboundScan setDeletedAt(Object deletedAt) {
    this.deletedAt = deletedAt;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public InboundScan setId(Integer id) {
    this.id = id;
    return this;
  }

  public Object getActiveOrder() {
    return activeOrder;
  }

  public InboundScan setActiveOrder(Object activeOrder) {
    this.activeOrder = activeOrder;
    return this;
  }

  public Object getUpdatedAt() {
    return updatedAt;
  }

  public InboundScan setUpdatedAt(Object updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public Object getActiveRoute() {
    return activeRoute;
  }

  public InboundScan setActiveRoute(Object activeRoute) {
    this.activeRoute = activeRoute;
    return this;
  }
}