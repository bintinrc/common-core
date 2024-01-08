package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestProductException;

public class NvTestCoreTransactionStatusMismatchException extends NvTestProductException {

  public NvTestCoreTransactionStatusMismatchException() {
  }

  public NvTestCoreTransactionStatusMismatchException(String message) {
    super(message);
  }

  public NvTestCoreTransactionStatusMismatchException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreTransactionStatusMismatchException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreTransactionStatusMismatchException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
