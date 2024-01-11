package co.nvqa.common.core.model.messaging;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
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
public class SendSmsRequest {

  private String template;
  @JsonProperty("order_details")
  private List<Map<String, Object>> orderDetails;
}
