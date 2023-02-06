package co.nvqa.common.core.model;

import java.util.Date;
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
public class MessagingHistoryResponse {

  private Long id;
  private Long createdAt;
  private Date updatedAt;
  private Date deletedAt;
  private String toNumber;
  private String message;
  private String vendorUuid;
  private Integer status;
  private String source;
  private String route;
  private String currency;
  private String rate;
  private Long orderId;
  private String systemId;
  private String sentTime;

}
