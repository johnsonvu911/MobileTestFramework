package base;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.configuration.Theme;
import datasets.AppInfo;
import helpers.ReportHelper;
import org.jetbrains.annotations.NotNull;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class Reporter extends ReportHelper {
    @BeforeTest
    public void configReport() {
        reporter.config().setEncoding("utf-8");
        reporter.config().setDocumentTitle("QA Automation Report");
        reporter.config().setReportName("QA Automation Test Results");
        reporter.config().setTheme(Theme.DARK);
        report.attachReporter(reporter);
    }
    @AfterTest
    public void markupReport() {
        report.setSystemInfo("Environment", "QA");
        report.setSystemInfo("Platform", platformName);
        report.setSystemInfo("Platform Version", deviceVersion);
        report.setSystemInfo("Device Name", deviceName);
        report.setSystemInfo("App Name", AppInfo.getValue(AppInfo.APP_NAME));
    }
    @AfterTest(dependsOnMethods = {"markupReport"})
    public void flushReport() {
        report.flush();
    }
    @BeforeMethod(groups = {"smokeTest", "regression"})
    public void getTestCaseName(@NotNull ITestResult result) {
        testCaseName = report.createTest(result.getMethod().getDescription());
    }
    @AfterMethod(groups = {"smokeTest", "regression"})
    public void markupReport(@NotNull ITestResult result) {
        String testCaseDesc = result.getMethod().getDescription();
        if(result.getStatus() == ITestResult.SUCCESS) {
            testCaseName.log(Status.PASS, MarkupHelper.createLabel(testCaseDesc, ExtentColor.GREEN));
        } else if (result.getStatus() == ITestResult.FAILURE) {
            testCaseName.log(Status.FAIL, MarkupHelper.createLabel(testCaseDesc, ExtentColor.RED));
        } else if (result.getStatus() == ITestResult.SKIP) {
            testCaseName.log(Status.SKIP, MarkupHelper.createLabel(testCaseDesc, ExtentColor.BROWN));
        }
    }
}
