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
}