package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestProductException;

public class NvTestCoreDbWaypointNotFoundException extends NvTestProductException {

  public NvTestCoreDbWaypointNotFoundException() {
  }

  public NvTestCoreDbWaypointNotFoundException(String message) {
    super(message);
  }

  public NvTestCoreDbWaypointNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreDbWaypointNotFoundException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreDbWaypointNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
