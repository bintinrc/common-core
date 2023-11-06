package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestEnvironmentException;

public class NvTestCoreOrderKafkaLagException extends NvTestEnvironmentException {

  public NvTestCoreOrderKafkaLagException() {
  }

  public NvTestCoreOrderKafkaLagException(String message) {
    super(message);
  }

  public NvTestCoreOrderKafkaLagException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreOrderKafkaLagException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreOrderKafkaLagException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
