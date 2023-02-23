package co.nvqa.common.core.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RtsOrderRequest {

  private String address1;
  private String address2;
  private String city;
  private String contact;
  private String country;
  private String date;
  private String email;
  private String name;
  private Long orderId;
  private String postcode;
  private String reason;
  private Long timewindowId;
  private Double latitude;
  private Double longitude;
}
