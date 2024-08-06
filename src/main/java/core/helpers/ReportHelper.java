package core.helpers;

import core.base.Initiatives;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ReportHelper extends Initiatives {
    public ExtentSparkReporter reporter = new ExtentSparkReporter(
            System.getProperty("user.dir") +
                    File.separator + "reports" +
                    File.separator + "TestRun+Report.html");
    public static ExtentReports report = new ExtentReports();;
    public static ExtentTest testCaseName;
}