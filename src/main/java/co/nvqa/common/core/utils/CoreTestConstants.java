package co.nvqa.common.core.utils;

import co.nvqa.common.utils.PropertiesReader;
import co.nvqa.common.utils.StandardTestConstants;

public class CoreTestConstants extends PropertiesReader {

  public static String DB_CORE_URL;

  static {
    loadProperties();
  }

  private static void loadProperties() {
    DB_CORE_URL = String.format("jdbc:mysql://%s:%s/core_%s_%s?characterEncoding=UTF-8",
        StandardTestConstants.NV_DATABASE_HOST,
        StandardTestConstants.NV_DATABASE_PORT,
        StandardTestConstants.NV_DATABASE_ENVIRONMENT,
        StandardTestConstants.NV_SYSTEM_ID);
  }

}
