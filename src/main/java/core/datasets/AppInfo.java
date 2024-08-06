package core.datasets;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

public enum AppInfo {
    APP_NAME,
    APP_ACTIVITY,
    APP_PACKAGE,
    BUNDLE_ID,
    APP_BUILD_NAME_IOS,
    APP_BUILD_NAME_ANDROID;

    private static final Map<AppInfo, String> values = new EnumMap<>(AppInfo.class);

    public static void loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = AppInfo.class.getClassLoader().getResourceAsStream("AppConfig.properties")) {
            if (input == null) {
                throw new IllegalArgumentException("AppConfig.properties file not found");
            }
            properties.load(input);

            String currentApp = properties.getProperty("CURRENT_APP");
            if (currentApp == null) {
                throw new IllegalArgumentException("CURRENT_APP is not defined in the properties file.");
            }

            for (AppInfo appInfo : AppInfo.values()) {
                String key = currentApp + "_" + appInfo.name();
                String value = properties.getProperty(key);
                if (value != null) {
                    values.put(appInfo, value);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getValue(AppInfo appInfo) {
        return values.get(appInfo);
    }
}
