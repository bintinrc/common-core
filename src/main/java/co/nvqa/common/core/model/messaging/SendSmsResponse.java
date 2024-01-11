package co.nvqa.common.core.model.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SendSmsResponse {

  private SendSmsData data;


  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SendSmsData {

    @JsonProperty("tracking_id")
    private String trackingId;

    private String message;

    @JsonProperty("long_message")
    private String longMessage;
  }

}
