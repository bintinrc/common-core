package co.nvqa.common.core.model.order;

import java.util.Map;
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
public class UserDetails {

  private String name;
  private String phoneNumber;
  private String email;
  private Map<String, String> address;

}
