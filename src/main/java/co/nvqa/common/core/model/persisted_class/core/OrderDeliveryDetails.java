package co.nvqa.common.core.model.persisted_class.core;


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
import org.apache.commons.lang3.time.DateFormatUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_delivery_details")
public class OrderDeliveryDetails extends DataEntity<OrderDeliveryDetails> {

  @Id
  private Long id;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "original_transaction_end_time")
  private Date originalTransactionEndTime;

  public OrderDeliveryDetails(Map<String, ?> data) {
    super(data);
  }

  public String originalTransactionEndDate_yyyy_MM_dd() {
    return DateFormatUtils.format(originalTransactionEndTime, "yyyy-MM-dd");
  }
}
