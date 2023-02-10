package co.nvqa.common.core.model.persisted_class.events;

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
@Table(name = "pickup_events")
public class PickupEvents extends DataEntity<PickupEvents> {

  @Id
  @Column(name = "id")
  private Long id;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "data")
  private String data;
  @Column(name = "event_time")
  private String eventTime;
  @Column(name = "pickup_id")
  private Long pickupId;
  @Column(name = "pickup_type")
  private Integer pickupType;
  @Column(name = "system_id")
  private String systemId;
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

  public PickupEvents() {
  }

  public PickupEvents(Map<String, ?> data) {
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

  public String getEventTime() {
    return eventTime;
  }

  public void setEventTime(String eventTime) {
    this.eventTime = eventTime;
  }

  public Long getPickupId() {
    return pickupId;
  }

  public void setPickupId(Long pickupId) {
    this.pickupId = pickupId;
  }

  public Integer getPickupType() {
    return pickupType;
  }

  public void setPickupType(Integer pickupType) {
    this.pickupType = pickupType;
  }

  public String getSystemId() {
    return systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
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