package co.nvqa.common.core.model.order;

import co.nvqa.common.model.DataEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchOrderInfo extends DataEntity<BatchOrderInfo> {

  private Long batch_id;
  private List<Order> orders;

}
