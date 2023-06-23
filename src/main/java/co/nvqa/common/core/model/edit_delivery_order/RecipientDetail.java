package co.nvqa.common.core.model.edit_delivery_order;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipientDetail {
  private String name;
  private String email;
  private String phoneNumber;
  private JsonNode address;
}
