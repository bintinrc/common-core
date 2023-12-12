package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestCodeException;

public class NvTestCoreCastingErrorException extends NvTestCodeException {

  public NvTestCoreCastingErrorException() {
  }

  public NvTestCoreCastingErrorException(String message) {
    super(message);
  }

  public NvTestCoreCastingErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreCastingErrorException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreCastingErrorException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
