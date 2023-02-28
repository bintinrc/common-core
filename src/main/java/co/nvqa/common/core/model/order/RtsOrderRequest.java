package co.nvqa.common.core.model.order;

import lombok.AllArgsConstructor;
<<<<<<< HEAD
import lombok.Builder;
=======
>>>>>>> master
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
<<<<<<< HEAD
@Builder
@NoArgsConstructor
@AllArgsConstructor
=======
@AllArgsConstructor
@NoArgsConstructor
>>>>>>> master
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
<<<<<<< HEAD

  public RtsOrderRequest(String address1, String address2, String city, String contact,
      String country,
      String date, String email, String name, Long orderId, String postcode, String reason,
      Long timewindowId) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.contact = contact;
    this.country = country;
    this.date = date;
    this.email = email;
    this.name = name;
    this.orderId = orderId;
    this.postcode = postcode;
    this.reason = reason;
    this.timewindowId = timewindowId;
  }

=======
>>>>>>> master
}
