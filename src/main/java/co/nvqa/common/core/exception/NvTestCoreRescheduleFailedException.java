package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestProductException;

public class NvTestCoreRescheduleFailedException extends NvTestProductException {

  public NvTestCoreRescheduleFailedException() {
  }

  public NvTestCoreRescheduleFailedException(String message) {
    super(message);
  }

  public NvTestCoreRescheduleFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreRescheduleFailedException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreRescheduleFailedException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
