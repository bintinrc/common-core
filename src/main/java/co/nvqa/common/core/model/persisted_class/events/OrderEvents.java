package co.nvqa.common.core.model.persisted_class.events;

import co.nvqa.common.model.DataEntity;
import java.util.Date;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sergey Mishanin
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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

  public OrderEvents(Map<String, ?> data) {
    fromMap(data);
  }

}