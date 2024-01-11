package co.nvqa.common.core.model;

import co.nvqa.common.model.DataEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrinterSettings extends DataEntity<PrinterSettings> {

  private Long id;

  private String name;

  private String ipAddress;

  private String version;

  @JsonProperty("is_default")
  private boolean isDefault;

  public PrinterSettings(Map<String, ?> data) {
    fromMap(data);
  }
}