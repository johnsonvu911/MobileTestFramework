package base;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static base.Initiatives.*;

public class TestSuiteListener implements ITestListener, IAnnotationTransformer {
    @Override
    public void onTestStart (ITestResult result) {
        ITestListener.super.onTestStart(result);
    }

    @Override
    public void onTestSuccess (ITestResult result) {
        ITestListener.super.onTestSuccess(result);
    }
    @Override
    public void onTestFailure (ITestResult result) {
        ITestListener.super.onTestFailure(result);

        String folderPath = String.format("screenshots%s%s", File.separator, LocalDate.now());
        String currentDateTime = LocalDateTime.now().toString().replace(":", "-");
        String fileName = String.format("%s%s_%s_%s.png", File.separator, result.getName(), platformName, currentDateTime);

        File file = ((TakesScreenshot) driver[Utils.getDeviceIndex()]).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(folderPath,  fileName));
            System.out.println("Saving screenshot to: " + folderPath + fileName); // For Debug only.
        } catch (IOException e) {
            System.out.println("fail to copy screenshot file to folder: " + e.getMessage());
        }
    }
    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);

        logger.info("PASSED TEST CASES");
        context.getPassedTests().getAllResults()
                .forEach(result -> {logger.info(result.getName());});

        logger.info("FAILED TEST CASES");
        context.getFailedTests().getAllResults()
                .forEach(result -> {logger.info(result.getName());});

        logger.info(
                "Test completed on: " + context.getEndDate().toString());
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
