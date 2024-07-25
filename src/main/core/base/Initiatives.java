package base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import datasets.GlobalData;
import helpers.CommandExecutor;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.util.Strings;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static base.Utils.readXMLFile;

public class Initiatives {
    public static final Logger logger = LoggerFactory.getLogger(Initiatives.class);
    public static final String pathToDevicesFile = "src" + File.separator + "main" + File.separator + "core" + File.separator + "devices.xml";
    public static List<Map<String, String>> device = readXMLFile(pathToDevicesFile, "device");
    private static final int totalDevices = Integer.parseInt(readXMLFile(pathToDevicesFile, "liquidpay").get(0).get("totaldevices"));
    public static AppiumDriver[] driver = new AppiumDriver[totalDevices];
    public static AppiumDriverLocalService[] appiumService = new AppiumDriverLocalService[totalDevices];
    public static String platformName;
    protected static int platformIndex;
    protected static String profileName;
    public static WebDriver webDriver;

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
        platformName = device.get(index).getOrDefault("platformname", "invalid tag name");
        profileName = device.get(index).getOrDefault("profilename", "invalid tag name");
    }
    public void setupMobileEnvironment(String testDevice, String searchKeyword, String deviceName, String bundlePackageId, String appActivity, String pathToAppFile) {
        int index = Integer.parseInt(testDevice);
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

            if (isAppInstalled(deviceName, searchKeyword)) {
                capabilities.setCapability(appId, bundlePackageId);
            } else {
                capabilities.setCapability(appId, bundlePackageId);
                capabilities.setCapability("app", pathToAppFile);
            }
        } catch (IOException e) {
            logger.error("Exception with start mobile session: {}", e.getMessage());
        }
        String udid = CommandExecutor.executeCommand("xcrun simctl list devices | grep 'Booted' | cut -d '(' -f 2 | cut -d ')' -f 1");
        if (platformName.equalsIgnoreCase("ios")) {
            platformIndex = 0;
            capabilities.setCapability("udid", udid);
            driver[index] = new IOSDriver(appiumService[index].getUrl(), capabilities);
        } else if (platformName.equalsIgnoreCase("android")) {
            platformIndex = 1;
            capabilities.setCapability("appActivity", appActivity);
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
        driver[index].quit();
    }
    public static void setupWebDriver(@NotNull String browserName) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments ("--remote-allow-origins=*");
        switch (browserName) {
            case GlobalData.Chrome:
                setWebDriverProperty(browserName);
                webDriver = new ChromeDriver(options);
                break;
            case GlobalData.Firefox:
                setWebDriverProperty(browserName);
                webDriver = new FirefoxDriver();
                break;
            case GlobalData.Edge:
                setWebDriverProperty(browserName);
                webDriver = new EdgeDriver();
                break;
            case GlobalData.Safari: webDriver = new SafariDriver(); break;
            default: System.out.printf("The web browser of %s is not supported currently", browserName);
        }
    }
    private static void setWebDriverProperty(@NotNull String browserName) {
        String pathToDriverFile = "drivers" + File.separator + "%s" + File.separator + browserName + File.separator + "webdriver";
        final String OSName = System.getProperty("os.name");;
        final String OSArch = System.getProperty("os.arch");
        String webDriverKey = browserName.equalsIgnoreCase("firefox") ? "webdriver.gecko.driver" : String.format("webdriver.%s.driver", browserName);
        if (OSName.contains("Mac OS X")) {
            if (OSArch.contains("aarch64")) {
                System.setProperty(webDriverKey, Utils.getPathToFileInResources(String.format(pathToDriverFile, "mac+arm")));
            } else {
                System.setProperty(webDriverKey, Utils.getPathToFileInResources(String.format(pathToDriverFile, "mac+intel")));
            }
        } else if (OSName.contains("Windows")) {
            System.setProperty(webDriverKey, Utils.getPathToFileInResources(String.format(pathToDriverFile + ".exe", "win")));
        } else if(OSName.contains("Linux")) {
            System.setProperty(webDriverKey, Utils.getPathToFileInResources(String.format(pathToDriverFile, "linux")));
        } else {
            System.out.printf("The OS of %s is not supported currently%n", OSName);
        }
    }
    private boolean isAppInstalled(String deviceName, String searchKeyword) {
        String checkCommandIos = String.format("xcrun simctl listapps \"%s\" | grep \"%s\"", deviceName, searchKeyword);
        String checkCommandAndroid = String.format("adb shell pm list packages | grep %s", searchKeyword);
        String checkOutput = "";
        try {
            String checkCommand = platformName.equalsIgnoreCase("ios") ? checkCommandIos : checkCommandAndroid;
            // Create a ProcessBuilder instance with the shell and its arguments
            ProcessBuilder checkBuilder = new ProcessBuilder("/bin/bash", "-c", checkCommand);
            // Start the process and get a Process instance
            Process checkProcess = checkBuilder.start();
            logger.info("Executing check command: {}", checkBuilder.command());
            // Execute the check command and read the output
            BufferedReader checkReader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
            checkOutput = checkReader.readLine();
            checkReader.close();
            checkProcess.waitFor();

        } catch (IOException | InterruptedException e) {
            logger.error("Exception error with checking app installation: {}", e.getMessage());
        }
        return !Strings.isNullOrEmpty(checkOutput);
    }
}