package co.nvqa.common.core.model.other;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoreExceptionResponse {

  private Long id;
  private Error data;
  private String status;

  @Getter
  @Setter
  public static class Error {

    private Integer code;
    private String nvErrorCode;
    private List<String> messages = new ArrayList<>();
    private String application;
    private String description;
    private String title;
    private Data data;

    @Getter
    @Setter
    public static class Data {
      private String message;
    }
  }

}
