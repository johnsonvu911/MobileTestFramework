package helpers;

import base.Initiatives;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.jetbrains.annotations.NotNull;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.File;

public class ReportHelper extends Initiatives {
    public ExtentSparkReporter reporter = new ExtentSparkReporter(
            System.getProperty("user.dir") +
                    File.separator + "reports" +
                    File.separator + "TestRun+Report.html");
    public static ExtentReports report = new ExtentReports();;
    public static ExtentTest testCaseName;

    @BeforeTest
    public void configReport() {
        reporter.config().setEncoding("utf-8");
        reporter.config().setDocumentTitle("QA Automation Report");
        reporter.config().setReportName("QA Automation Test Results");
        reporter.config().setTheme(Theme.DARK);
        report.attachReporter(reporter);
    }
    @AfterTest
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