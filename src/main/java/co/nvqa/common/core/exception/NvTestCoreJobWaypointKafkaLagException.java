package co.nvqa.common.core.exception;

import co.nvqa.common.utils.NvTestEnvironmentException;

public class NvTestCoreJobWaypointKafkaLagException extends NvTestEnvironmentException {

  public NvTestCoreJobWaypointKafkaLagException() {
  }

  public NvTestCoreJobWaypointKafkaLagException(String message) {
    super(message);
  }

  public NvTestCoreJobWaypointKafkaLagException(String message, Throwable cause) {
    super(message, cause);
  }

  public NvTestCoreJobWaypointKafkaLagException(Throwable cause) {
    super(cause);
  }

  public NvTestCoreJobWaypointKafkaLagException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
