package co.nvqa.common.core.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Daniel Joi Partogi Hutapea
 * <p>
 * JSON format: camel case
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tags {

  private Tag tag;
  private List<Tag> tags;

}
