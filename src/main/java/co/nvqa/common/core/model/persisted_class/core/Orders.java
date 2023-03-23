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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "orders")
public class Orders extends DataEntity<Orders> {

  @Id
  private Long id;
  @Column(name = "tracking_id")
  private String trackingId;
  private String status;
  @Column(name = "granular_status")
  private String granularStatus;
  private Double weight;
  @Column(name = "deleted_at")
  private Double deletedAt;
  private String data;

  @Column(name = "from_address1")
  private String fromAddress1;
  @Column(name = "from_address2")
  private String fromAddress2;
  @Column(name = "from_postcode")
  private String fromPostcode;
  @Column(name = "from_city")
  private String fromCity;
  @Column(name = "from_state")
  private String fromState;
  @Column(name = "from_country")
  private String fromCountry;
  @Column(name = "from_name")
  private String fromName;
  @Column(name = "from_contact")
  private String fromContact;
  @Column(name = "from_email")
  private String fromEmail;

  @Column(name = "to_address1")
  private String toAddress1;
  @Column(name = "to_address2")
  private String toAddress2;
  @Column(name = "to_postcode")
  private String toPostcode;
  @Column(name = "to_city")
  private String toCity;
  @Column(name = "to_state")
  private String toState;
  @Column(name = "to_country")
  private String toCountry;
  @Column(name = "to_name")
  private String toName;
  @Column(name = "to_contact")
  private String toContact;
  @Column(name = "to_email")
  private String toEmail;

  public Orders(Map<String, ?> data) {
    super(data);
  }

}