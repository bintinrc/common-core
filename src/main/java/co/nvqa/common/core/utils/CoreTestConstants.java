package co.nvqa.common.core.utils;

import co.nvqa.common.utils.PropertiesReader;
import co.nvqa.common.utils.StandardTestConstants;
import org.apache.commons.lang3.StringUtils;

public class CoreTestConstants extends PropertiesReader {

  public static String DB_CORE_URL;

  static {
    loadProperties();
  }

  private static void loadProperties() {
    DB_CORE_URL = String.format("jdbc:mysql://%s:%s/core_%s_%s?characterEncoding=UTF-8",
        StringUtils.lowerCase(StandardTestConstants.NV_DATABASE_HOST),
        StandardTestConstants.NV_DATABASE_PORT,
        StringUtils.lowerCase(StandardTestConstants.NV_DATABASE_ENVIRONMENT),
        StringUtils.lowerCase(StandardTestConstants.NV_SYSTEM_ID));
  }

}
