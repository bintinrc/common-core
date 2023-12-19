package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestEnvironmentException;

public class NvTestCoreMilkrunGroupNotFoundException extends NvTestEnvironmentException {

  public NvTestCoreMilkrunGroupNotFoundException() {
  }

  public NvTestCoreMilkrunGroupNotFoundException(String message) {
    super(message);
  }

  public NvTestCoreMilkrunGroupNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreMilkrunGroupNotFoundException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreMilkrunGroupNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
