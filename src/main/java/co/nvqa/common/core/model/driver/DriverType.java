package co.nvqa.common.core.model.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverType {

  private String id;
  private String name;
  private Conditions conditions;
  private String createdAt;
  private String updatedAt;
  private String deletedAt;
  private String systemId;
  private String uuid;

}
