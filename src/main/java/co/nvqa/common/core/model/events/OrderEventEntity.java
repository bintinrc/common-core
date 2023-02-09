package co.nvqa.common.core.model.events;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
import java.util.Map;

/**
 * @author Sergey Mishanin
 */
public class OrderEventEntity extends DataEntity<OrderEventEntity> {

  private Long id;
  private String systemId;
  private Long orderId;
  private Integer type;
  private Date time;
  private Integer scope;
  private Integer userId;
  private String userGrantType;
  private String userName;
  private String userEmail;
  private String data;

  public OrderEventEntity() {
  }

  public OrderEventEntity(Map<String, ?> data) {
    fromMap(data);
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  public long getOrderId() {
    return orderId;
  }

  public void setOrderId(long orderId) {
    this.orderId = orderId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public Integer getScope() {
    return scope;
  }

  public void setScope(int scope) {
    this.scope = scope;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUserGrantType() {
    return userGrantType;
  }

  public void setUserGrantType(String userGrantType) {
    this.userGrantType = userGrantType;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
