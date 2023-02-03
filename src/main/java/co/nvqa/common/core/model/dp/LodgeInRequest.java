package co.nvqa.common.core.model.dp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
