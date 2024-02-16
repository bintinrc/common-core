package co.nvqa.common.core.model.route;

import co.nvqa.common.core.model.other.CoreExceptionResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkAddPickupJobToRouteResponse {

  private List<CoreExceptionResponse> successfulJobs;
  private List<CoreExceptionResponse> failedJobs;
}
