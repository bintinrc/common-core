package co.nvqa.common.core.model.order;

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
public class RtsOrderRequest {

  private String address1;
  private String address2;
  private String city;
  private String contact;
  private String country;
  private String date;
  private String email;
  private String name;
  private String postcode;
  private String reason;
  private Long timewindowId;
  private Double latitude;
  private Double longitude;

  public RtsOrderRequest(String address1, String address2, String city, String contact,
      String country,
      String date, String email, String name, String postcode, String reason,
      Long timewindowId) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.contact = contact;
    this.country = country;
    this.date = date;
    this.email = email;
    this.name = name;
    this.postcode = postcode;
    this.reason = reason;
    this.timewindowId = timewindowId;
  }
}
