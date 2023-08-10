package co.nvqa.common.core.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartRouteRequest {

  private String userId;
  private String userName;
  private String userGrantType;
  private String userEmail;
}
