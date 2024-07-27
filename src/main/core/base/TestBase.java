package base;

import datasets.AppInfo;
import org.testng.annotations.*;
import java.io.File;
import java.util.Map;

public class TestBase extends Initiatives {
    public static final String testDeviceIndex = "0"; // For debug purposes.
    protected static Map<String, String> appCofigs = Utils.getPropertyValues("AppConfig");
    protected static String appName = AppInfo.getValue(AppInfo.APP_NAME).trim();
    protected static String bundleId = AppInfo.getValue(AppInfo.BUNDLE_ID).trim();
    protected static String appPackage = AppInfo.getValue(AppInfo.APP_PACKAGE).trim();
    protected static String appActivity = AppInfo.getValue(AppInfo.APP_ACTIVITY).trim();
    protected static String appBuildNameIOS = AppInfo.getValue(AppInfo.APP_BUILD_NAME_IOS).trim();
    protected static String appBuildNameAndroid = AppInfo.getValue(AppInfo.APP_BUILD_NAME_ANDROID).trim();

    @BeforeTest(groups = {"smokeTest", "regression"})
    @AfterTest(groups = {"smokeTest", "regression"})
    @Parameters({"device"})
    public void setupAppium(@Optional(testDeviceIndex) String device) {
        startAndStopAppium(device);
    }
    @BeforeMethod(groups = {"smokeTest", "regression"})
    @Parameters({"device"})
    public void startMobileSession(@Optional(testDeviceIndex) String device) {
        String searchKeyword;
        String pathToAppFile;
        String bundlePackageId;
        String pathToIosBuildFile = ".." + File.separator + ".." + File.separator + "appbuilds" + File.separator + appBuildNameIOS;
        String pathToAndroidBuildFile = ".." + File.separator + ".." + File.separator + "appbuilds" + File.separator + appBuildNameAndroid;

        if (platformName.equalsIgnoreCase("ios")) {
            searchKeyword = appName;
            pathToAppFile = Utils.getAbsolutePath(pathToIosBuildFile);
            bundlePackageId = bundleId;
        } else {
            searchKeyword = appPackage;
            pathToAppFile = Utils.getAbsolutePath(pathToAndroidBuildFile);
            bundlePackageId = appName;
        }
        setupMobileEnvironment(device, searchKeyword, bundlePackageId, appActivity, pathToAppFile);
    }

    @AfterMethod(groups = {"smokeTest", "regression"}, alwaysRun = true)
    @Parameters({"device"})
    public void endMobileSession(@Optional(testDeviceIndex) String device) {
        terminateApp(device, bundleId, appPackage);
    }
}
