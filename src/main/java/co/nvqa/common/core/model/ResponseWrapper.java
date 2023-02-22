package co.nvqa.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Daniel Joi Partogi Hutapea
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper<T> {

  private String nvVersion;
  private T data;
  private String uuid;
}
