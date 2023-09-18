package co.nvqa.common.core.utils;

import co.nvqa.common.utils.DateUtil;
import co.nvqa.common.utils.StandardTestConstants;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CoreTestUtils {

  public static ZonedDateTime getDateForToday() {
    return DateUtil.getStartOfDay(DateUtil.getDate());
  }

  public static ZonedDateTime getDateForNextDay() {
    return DateUtil.getStartOfDay(DateUtil.getDate()).plusDays(1);
  }

  public static String generateUniqueId() {
    ZonedDateTime zdt = DateUtil.getDate(ZoneId.of(StandardTestConstants.DEFAULT_TIMEZONE));
    long currentMillis = zdt.toInstant().toEpochMilli();
    String uniqueString = co.nvqa.common.utils.RandomUtil.randomString(256);
    return uniqueString.substring(0, 13) + "-" + String.valueOf(currentMillis).substring(2, 11);
  }
}
