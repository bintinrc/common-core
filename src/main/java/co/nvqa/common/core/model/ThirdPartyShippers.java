package co.nvqa.common.core.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("unused")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyShippers {

  private Integer id;
  private String code;
  private String name;
  private String url;

}
