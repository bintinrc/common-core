package co.nvqa.common.core.model.persisted_class.core;

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
@Table(name = "order_details")
public class OrderDetails {
  @Id
  private Long id;

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "service_type")
  private String serviceType;

  @Column(name = "service_level")
  private String serviceLevel;

  @Column(name = "package_content")
  private String packageContent;

  @Column(name = "sort_code")
  private String sortCode;

  @Column(name = "to_3pl")
  private String to3pl;

  @Column(name = "parent_shipper_id")
  private Long parentShipperId;

}