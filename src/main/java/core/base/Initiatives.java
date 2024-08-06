package core.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.datasets.AppInfo;
import core.helpers.AppHelper;
import core.helpers.CommandExecutor;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.util.Strings;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static core.base.Utils.readXMLFile;

public class Initiatives {
    public static final Logger logger = LoggerFactory.getLogger(Initiatives.class);
    public static final String pathToDevicesFile = "src" + File.separator + "main" + File.separator + "core" + File.separator + "devices.xml";
    public static List<Map<String, String>> device = readXMLFile(pathToDevicesFile, "device");
    private static final int totalDevices = Integer.parseInt(readXMLFile(pathToDevicesFile, "testrocket").get(0).get("totaldevices"));
    public static AppiumDriver[] driver = new AppiumDriver[totalDevices];
    public static AppiumDriverLocalService[] appiumService = new AppiumDriverLocalService[totalDevices];
    public static String platformName;
    protected static int platformIndex;
    protected static String profileName;
    protected static String deviceVersion;
    protected static String deviceName;
    // Command line to get UDID from active device.
    private static final String getUdidCmd = "xcrun simctl list devices | grep 'Booted' | cut -d '(' -f 2 | cut -d ')' -f 1";

    public Initiatives() {
        super();
    }
    public void startAndStopAppium(String testDevice) {
        int index = Integer.parseInt(testDevice);
        int appiumPort = Integer.parseInt(device.get(index).get("appiumport"));
        try (AppiumDriverLocalService service = new AppiumServiceBuilder()
                .usingPort(appiumPort)
                .withArgument(GeneralServerFlag.BASEPATH, "/")
                .withArgument(() -> "--use-plugins", "gestures")
                .build()) {

            appiumService[index] = service;
            appiumService[index].start();
        } catch (Exception e) {
            logger.error("Can't start appium service: {}", e.getMessage());
        } finally {
            if (appiumService[index] != null && appiumService[index].isRunning()) {
                appiumService[index].stop();
            }
        }
        platformName = device.get(index).getOrDefault("deviceos", "invalid tag name");
        profileName = device.get(index).getOrDefault("profilename", "invalid tag name");
    }
    public void setupMobileEnvironment(String testDevice, String searchKeyword, String bundlePackageId, String appActivity, String pathToAppFile) {
        int index = Integer.parseInt(testDevice);
        deviceName = device.get(index).get("devicename");
        deviceVersion = device.get(index).get("deviceversion");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String appId = platformName.equalsIgnoreCase("ios") ? "bundleId" : "appPackage";
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("profiles" + File.separator + profileName + ".json");

        if (inputStream == null) {
            logger.error("File not found: {}.json", profileName);
            throw new IllegalArgumentException("File not found: " + profileName + ".json");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> deviceInfo = gson.fromJson(reader, mapType);
            for (Map.Entry<String, String> entry : deviceInfo.entrySet()) {
                capabilities.setCapability(entry.getKey(), entry.getValue());
            }
            capabilities.setCapability("noReset", false);
            capabilities.setCapability("systemPort", 8300);
        } catch (IOException e) {
            logger.error("Exception with start mobile session: {}", e.getMessage());
        }
        if (platformName.equalsIgnoreCase("ios")) {
            platformIndex = 0;
            if (AppHelper.isAppInstalled(deviceName, searchKeyword)) {
                capabilities.setCapability(appId, bundlePackageId);
            } else {
                capabilities.setCapability(appId, bundlePackageId);
                capabilities.setCapability("app", pathToAppFile);
            }
            String udid = CommandExecutor.executeCommand(getUdidCmd);
            capabilities.setCapability("udid", udid);
            driver[index] = new IOSDriver(appiumService[index].getUrl(), capabilities);
        } else if (platformName.equalsIgnoreCase("android")) {
            platformIndex = 1;
            if (!AppHelper.isAppInstalled(deviceName, searchKeyword)) {
                capabilities.setCapability("app", pathToAppFile);
            } else {
                capabilities.setCapability("appPackage", bundlePackageId);
                capabilities.setCapability("appActivity", appActivity);
            }
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);
            driver[index] = new AndroidDriver(appiumService[index].getUrl(), capabilities);
        }
    }
    public void terminateApp(String testDevice, String bundleId, String appPackage) {
        int index = Integer.parseInt(testDevice);
        if (platformName.equalsIgnoreCase("ios")) {
            ((IOSDriver) driver[index]).terminateApp(bundleId);
        } else if (platformName.equalsIgnoreCase("android")) {
            ((AndroidDriver) driver[index]).terminateApp(appPackage);
        }
        if(driver[index] != null) {
            driver[index].quit();
        }
    }
}