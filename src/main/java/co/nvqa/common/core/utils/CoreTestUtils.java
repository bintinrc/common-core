package co.nvqa.common.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class CoreTestUtils {

  public static LocalDateTime getRouteDateForToday() {
    return LocalDate.now().atStartOfDay();
  }

  public static LocalDateTime getRouteDateForNextDay() {
    return LocalDate.now().plusDays(1).atStartOfDay();
  }

}
