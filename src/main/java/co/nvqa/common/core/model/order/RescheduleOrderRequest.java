package co.nvqa.common.core.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JSON example at Jan 24th 2023:
 * <p>
 * {"contact":"+6598980044","email":"customer.parcel.ld9q0m99@ninjavan.co","name":"C-P-LD9Q0M99
 * Customer","date":"2023-01-25","time_window":-1}
 * </p>
 * <p>
 * date is the mandatory field for rescheduling
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleOrderRequest {

  private String contact;
  private String date; // format: 2023-01-25
  private String email;
  private String name;
  private Integer timeWindow;
  @JsonProperty("address_1")
  private String address1;
  @JsonProperty("address_2")
  private String address2;
  private String postalCode;
  private String city;
  private String country;
  private Double latitude;
  private Double longitude;


}
