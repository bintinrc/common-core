package co.nvqa.common.core.model.batch_update_pods;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reschedule {

  private String address1;
  private String address2;
  private String country;
  private String date;
  private Integer timeWindow;
  private String postalCode;

}
