package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestEnvironmentException;

public class NvTestCoreMilkrunPendingTaskNotFoundException extends NvTestEnvironmentException {

  public NvTestCoreMilkrunPendingTaskNotFoundException() {
  }

  public NvTestCoreMilkrunPendingTaskNotFoundException(String message) {
    super(message);
  }

  public NvTestCoreMilkrunPendingTaskNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreMilkrunPendingTaskNotFoundException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreMilkrunPendingTaskNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
