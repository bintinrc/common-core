package co.nvqa.common.core.model.reservation;

import co.nvqa.common.core.model.other.CoreExceptionResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkRouteReservationResponse {

  private List<CoreExceptionResponse> successfulJobs;
  private List<CoreExceptionResponse> failedJobs;

}
