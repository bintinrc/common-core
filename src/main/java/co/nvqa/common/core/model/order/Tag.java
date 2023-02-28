package co.nvqa.common.core.model.order;

import co.nvqa.common.model.DataEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag extends DataEntity<Tag> {

  private Long id;
  private String name;
  private String description;
  private String createdAt;
  private String updatedAt;
  private String deletedAt;
  private Boolean editable;

  public Tag(Map<String, ?> data) {
    fromMap(data);
  }

}
