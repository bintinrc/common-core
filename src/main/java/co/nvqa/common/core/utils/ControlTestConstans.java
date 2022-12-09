package co.nvqa.common.core.utils;

import co.nvqa.common.utils.PropertiesReader;
import co.nvqa.common.utils.StandardTestConstants;

public class ControlTestConstans extends PropertiesReader {

  public static String DB_CONTROL_URL;

  static {
    loadProperties();
  }

  private static void loadProperties() {
    DB_CONTROL_URL = String.format("jdbc:mysql://%s:%s/control_%s_gl?characterEncoding=UTF-8",
        StandardTestConstants.NV_DATABASE_HOST,
        StandardTestConstants.NV_DATABASE_PORT,
        StandardTestConstants.NV_DATABASE_ENVIRONMENT,
        StandardTestConstants.NV_SYSTEM_ID);
  }

}
