package co.nvqa.common.core.model.dp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LodgeInRequest {

  private Long orderId;
  private String trackingId;
  private String fromName;
  private String fromEmail;
  private String fromContact;
  private String fromCity;
  private String fromState;
  private String fromPostcode;
  private String fromCountry;
  private String fromAddress1;
  private String fromAddress2;
  private Long distributionPointId;

}
