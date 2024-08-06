package core.base;

import android.content.Context;
import core.datasets.AppInfo;
import core.helpers.AppHelper;
import org.testng.annotations.*;
import java.io.File;
import java.util.Map;

public class TestBase extends Initiatives {
    public static final String testDeviceIndex = "1"; // For debug purposes.
    protected String bundleId = "";
    protected String appPackage = "";
    protected String appActivity = "";

    public TestBase() {
        super();
    }
    @BeforeTest(groups = {"smokeTest", "regression"})
    @AfterTest(groups = {"smokeTest", "regression"})
    @Parameters({"device"})
    public void setupAppium(@Optional(testDeviceIndex) String device) {
        startAndStopAppium(device);
    }
    @BeforeMethod(groups = {"smokeTest", "regression"})
    @Parameters({"device"})
    public void startMobileSession(@Optional(testDeviceIndex) String device) {
        // Load AppConfig.properties file for usage in somewhere else.
        AppInfo.loadProperties();
        String appName = AppInfo.getValue(AppInfo.APP_NAME).trim();
        bundleId = AppInfo.getValue(AppInfo.BUNDLE_ID).trim();
        appPackage = AppInfo.getValue(AppInfo.APP_PACKAGE).trim();
        appActivity = AppInfo.getValue(AppInfo.APP_ACTIVITY).trim();
        String appBuildNameIOS = AppInfo.getValue(AppInfo.APP_BUILD_NAME_IOS).trim();
        String appBuildNameAndroid = AppInfo.getValue(AppInfo.APP_BUILD_NAME_ANDROID).trim();
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
            bundlePackageId = appPackage;
        }
        setupMobileEnvironment(device, searchKeyword, bundlePackageId, appActivity, pathToAppFile);
    }

//    @AfterMethod(groups = {"smokeTest", "regression"})
    @Parameters({"device"})
    public void endMobileSession(@Optional(testDeviceIndex) String device) {
            terminateApp(device, bundleId, appPackage);
    }
}
