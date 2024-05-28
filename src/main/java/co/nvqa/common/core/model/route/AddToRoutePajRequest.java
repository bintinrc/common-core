package co.nvqa.common.core.model.route;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddToRoutePajRequest {

  private List<Long> jobIds;

}
