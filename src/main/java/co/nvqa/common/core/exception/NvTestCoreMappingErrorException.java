package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestCodeException;

public class NvTestCoreMappingErrorException extends NvTestCodeException {

  public NvTestCoreMappingErrorException() {
  }

  public NvTestCoreMappingErrorException(String message) {
    super(message);
  }

  public NvTestCoreMappingErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreMappingErrorException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreMappingErrorException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
