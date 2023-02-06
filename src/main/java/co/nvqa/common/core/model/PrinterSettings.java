package co.nvqa.common.core.model;

import co.nvqa.common.model.DataEntity;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrinterSettings extends DataEntity<PrinterSettings> {
  private Long id;
  private String name;
  private String ipAddress;
  private String version;
  private boolean isDefault;

  public PrinterSettings(Map<String, ?> data) {
    fromMap(data);
  }
}