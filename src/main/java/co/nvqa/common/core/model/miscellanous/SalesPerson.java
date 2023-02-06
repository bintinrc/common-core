package co.nvqa.common.core.model.miscellanous;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesPerson extends DataEntity<SalesPerson> {

  private Long id;
  private String createdAt;
  private String udpatedAt;
  private String deletedAt;
  private String code;
  private String name;

  public SalesPerson(Map<String, ?> data) {
    super(data);
  }
}
