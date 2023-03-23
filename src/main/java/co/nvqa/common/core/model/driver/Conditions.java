package co.nvqa.common.core.model.driver;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Conditions {

  @JsonProperty("0")
  private String zero;
  @JsonProperty("1")
  private String one;
  @JsonProperty("2")
  private String two;
  @JsonProperty("3")
  private String three;
  @JsonProperty("4")
  private String four;

}
