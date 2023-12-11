package co.nvqa.common.core.model.route;

import co.nvqa.common.model.DataEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JSON format: camel case
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteTag extends DataEntity<RouteTag> {

  private Long id;
  private String name;
  private String description;
  private String createdAt;
  private String updatedAt;
  private String deletedAt;
  private Boolean editable;

  public RouteTag(Map<String, ?> data) {
    fromMap(data);
  }


}
