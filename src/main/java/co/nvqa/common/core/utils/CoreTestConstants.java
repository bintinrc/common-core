package co.nvqa.common.core.utils;

import co.nvqa.common.utils.PropertiesReader;
import co.nvqa.common.utils.StandardTestConstants;
import org.apache.commons.lang3.StringUtils;

public class CoreTestConstants extends PropertiesReader {

  public static String DB_CORE_URL;
  public static String DB_EVENTS_URL;
  public static String DB_ROUTE_URL;
  public static Long THIRD_PARTY_SHIPPER_ID;
  public static String THIRD_PARTY_DEFAULT_TRACKING_ID;
  static {
    loadProperties();
  }

  private static void loadProperties() {
    DB_CORE_URL = String.format("jdbc:mysql://%s:%s/core_%s_%s?characterEncoding=UTF-8",
        StringUtils.lowerCase(StandardTestConstants.NV_DATABASE_HOST),
        StandardTestConstants.NV_DATABASE_PORT,
        StringUtils.lowerCase(StandardTestConstants.NV_DATABASE_ENVIRONMENT),
        StringUtils.lowerCase(StandardTestConstants.NV_SYSTEM_ID));
    DB_EVENTS_URL = String.format("jdbc:mysql://%s:%s/events_%s_%s?characterEncoding=UTF-8",
        StringUtils.lowerCase(StandardTestConstants.DB_HOST_TIDB),
        StandardTestConstants.DB_PORT_TIDB,
        StringUtils.lowerCase(StandardTestConstants.NV_DATABASE_ENVIRONMENT),
        "gl");
    DB_ROUTE_URL = String.format("jdbc:mysql://%s:%s/route_%s_%s?characterEncoding=UTF-8",
        StringUtils.lowerCase(StandardTestConstants.NV_DATABASE_HOST),
        StandardTestConstants.NV_DATABASE_PORT,
        StringUtils.lowerCase(StandardTestConstants.NV_DATABASE_ENVIRONMENT),
        "gl");
    THIRD_PARTY_DEFAULT_TRACKING_ID = getString("third-party-tracking-id");
    THIRD_PARTY_SHIPPER_ID = getLong("third-party-shipper-id");
  }

}
