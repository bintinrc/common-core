package co.nvqa.common.core.model;

import co.nvqa.common.model.DataEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodInbound extends DataEntity<CodInbound> {

  private Long routeId;
  private Double amountCollected;
  private String receiptNo;
  private String type;
  private String deletedAt;
}
