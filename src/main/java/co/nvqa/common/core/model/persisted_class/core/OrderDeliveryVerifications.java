package co.nvqa.common.core.model.persisted_class.core;


import co.nvqa.common.model.DataEntity;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_delivery_verifications")
public class OrderDeliveryVerifications extends DataEntity<OrderDeliveryVerifications> {

  @Id
  private Long id;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "allow_doorstep_dropoff")
  private Boolean allowDoorstepDropoff;
  @Column(name = "enforce_delivery_verification")
  private Boolean enforceDeliveryVerification;
  @Column(name = "delivery_verification_mode")
  private String deliveryVerificationMode;
  @Column(name = "delivery_verification_identity")
  private String deliveryVerificationIdentity;

  public OrderDeliveryVerifications(Map<String, ?> data) {
    super(data);
  }
}
