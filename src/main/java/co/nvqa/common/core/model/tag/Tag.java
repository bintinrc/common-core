package co.nvqa.common.core.model.tag;

import co.nvqa.common.model.DataEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Daniel Joi Partogi Hutapea
 * <p>
 * JSON format: camel case
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
