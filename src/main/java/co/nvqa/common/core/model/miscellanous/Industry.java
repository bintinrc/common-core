package co.nvqa.common.core.model.miscellanous;

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
public class Industry {

  private Long id;
  private String createdAt;
  private String updatedAt;
  private String deletedAt;
  private String name;

}
