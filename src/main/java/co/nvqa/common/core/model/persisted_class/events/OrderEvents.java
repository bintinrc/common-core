package co.nvqa.common.core.model.persisted_class.events;

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
@Table(name = "order_events")
public class OrderEvents extends DataEntity<OrderEvents> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "data")
  private String data;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "scope")
  private Integer scope;
  @Column(name = "system_id")
  private String systemId;
  @Column(name = "time")
  private Date time;
  @Column(name = "type")
  private Integer type;
  @Column(name = "user_email")
  private String userEmail;
  @Column(name = "user_grant_type")
  private String userGrantType;
  @Column(name = "user_id")
  private Integer userId;
  @Column(name = "user_name")
  private String userName;

  public OrderEvents() {
  }

  public OrderEvents(Map<String, ?> data) {
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

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Integer getScope() {
    return scope;
  }

  public void setScope(Integer scope) {
    this.scope = scope;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserGrantType() {
    return userGrantType;
  }

  public void setUserGrantType(String userGrantType) {
    this.userGrantType = userGrantType;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}