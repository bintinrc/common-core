package co.nvqa.common.core.model.other;

import co.nvqa.common.model.DataEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CoreExceptionResponse extends DataEntity<CoreExceptionResponse> {

  private Long id;
  private Error error;
  private String status;

  @Getter
  @Setter
  public static class Error extends DataEntity<Error> {

    private Long code;
    @JsonProperty("nvErrorCode")
    private String nvErrorCode;
    private List<String> messages = new ArrayList<>();
    private String application;
    private String description;
    private String title;
    private Data data;
    private Integer applicationExceptionCode;
    private String message;

    @Getter
    @Setter
    public static class Data extends DataEntity<Data> {
      private String message;
    }
  }

}
