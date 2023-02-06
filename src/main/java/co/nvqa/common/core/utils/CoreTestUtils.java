package co.nvqa.common.core.utils;

import co.nvqa.common.utils.DateUtil;
import java.time.ZonedDateTime;

public class CoreTestUtils {

  public static ZonedDateTime getDateForToday() {
    return DateUtil.getStartOfDay(DateUtil.getDate());
  }

  public static ZonedDateTime getDateForNextDay() {
    return DateUtil.getStartOfDay(DateUtil.getDate()).plusDays(1);
  }

}
