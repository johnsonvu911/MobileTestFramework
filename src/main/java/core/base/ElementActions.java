package core.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class ElementActions extends Initiatives {
    private static final int shortWait = 10;
    private static final int longWait = 30;

    public static void swipe(String deviceIndex, By elementHook, By elementForVisible, String direction) {
        int index = Integer.parseInt(deviceIndex);
        WebDriverWait wait = new WebDriverWait(driver[index], Duration.ofSeconds(shortWait));
        RemoteWebElement scrollView = new RemoteWebElement();
        while (!isElementDisplay(deviceIndex, elementForVisible)) {
            driver[index].executeScript("gesture: swipe", Map.of("elementId", scrollView.getId(),
                    "percentage", 10000,
                    "offset", 10000,
                    "direction", direction));
        }
    }
    // Waiting for element
    public static void waitForElement(String deviceIndex, By by, int timeLimitInSeconds) {
        int index = Integer.parseInt(deviceIndex);
        try {
            WebDriverWait wait = new WebDriverWait(driver[index], Duration.ofSeconds(timeLimitInSeconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("Element is not found: " + e.getMessage());
        }
    }
    public static boolean isElementDisplay(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);
        try {
            WebDriverWait wait = new WebDriverWait(driver[index], Duration.ofSeconds(shortWait));
            wait.until(presenceOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static void waitElementDisplayed(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);
        WebDriverWait wait = new WebDriverWait(driver[index], Duration.ofSeconds(shortWait));
        wait.until(presenceOfElementLocated(by));
    }
    public static boolean isElementExist(String deviceIndex, By by, int timeLimitInSeconds) {
        int index = Integer.parseInt(deviceIndex);
        try {
            WebDriverWait wait = new WebDriverWait(driver[index], Duration.ofSeconds(timeLimitInSeconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean waitForElementDisappear(String deviceIndex, By by, int timeLimitInSeconds) {
        int index = Integer.parseInt(deviceIndex);
        driver[index].manage().timeouts().implicitlyWait(Duration.ofSeconds(timeLimitInSeconds));
        try {
            WebDriverWait wait = new WebDriverWait(driver[index], Duration.ofSeconds(timeLimitInSeconds));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private static void waitElementClickable(int deviceIndex, By by) {
        WebDriverWait wait = new WebDriverWait(driver[deviceIndex], Duration.ofSeconds(longWait));
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }
    // Interaction with element
    public static void enter(String deviceIndex, By by, String inputText) {
        int index = Integer.parseInt(deviceIndex);
        try {
            waitElementDisplayed(deviceIndex, by);
            driver[index].findElement(by).clear();
            driver[index].findElement(by).sendKeys((inputText));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input for waitElementClickable: " + e.getMessage());
        }
    }
    public static void enterOnAndroid(String deviceIndex, By by, List<String> phoneNumber) {
        int index = Integer.parseInt(deviceIndex);
        driver[index].findElement(by).click();
        for (String number : phoneNumber) {
            if (Objects.equals(number, "+")) {
                ((AndroidDriver) driver[index]).pressKey(new KeyEvent(AndroidKey.valueOf("PLUS")));
            } else {
                ((AndroidDriver) driver[index]).pressKey(new KeyEvent(AndroidKey.valueOf("NUMPAD_" + number)));
            }
        }
        ((AndroidDriver) driver[index]).pressKey(new KeyEvent(AndroidKey.ENTER));
    }
    public static void click(String deviceIndex, String xpath) {
        int index = Integer.parseInt(deviceIndex);
        By element = new By.ByXPath(xpath);

        try {
            waitElementDisplayed(deviceIndex, element);
            driver[index].findElement(element).click();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input for waitElementClickable: " + e.getMessage());
        }
    }
    public static void click(String deviceIndex, By by, int numberOfClicks) {
        int index = Integer.parseInt(deviceIndex);
        for (int i = 0; i < numberOfClicks; i++) {
            driver[index].findElement(by).click();
        }
    }
    // Capture element
    public static WebElement captureElement(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);
        waitForElement(deviceIndex, by, shortWait * 2);
        return driver[index].findElement(by);
    }
    public static WebElement captureElement(String deviceIndex, By by, int timeLimitInSeconds) {
        int index = Integer.parseInt(deviceIndex);
        waitForElement(deviceIndex, by, timeLimitInSeconds);
        return driver[index].findElement(by);
    }
    public static void tap(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);

        JavascriptExecutor js = (JavascriptExecutor) driver[index];
        Map<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("x", "10");
        scrollObject.put("y", "10");
        scrollObject.put("element", ((RemoteWebElement) ElementActions.captureElement(deviceIndex, by)).getId());

        js.executeScript("mobile: tap", scrollObject);
    }
    public static void tapAtAPosition(String deviceIndex, int x, int y) {
        int index = Integer.parseInt(deviceIndex);
        JavascriptExecutor js = driver[index];
        Map<String, Object> params = new HashMap<>();
        params.put("x", x);
        params.put("y", y);
        js.executeScript("mobile: tap", params);
    }
//    public static void hideKeyboard(String deviceIndex, String platform) {
//        final By element = AppiumBy.iOSClassChain(GlobalData.DoneButtonKeyboard);
//        try {
//            if (platform.equalsIgnoreCase("ios")) {
//                click(deviceIndex, element);
//            } else if (platform.equalsIgnoreCase("android")) {
//                ((AndroidDriver) driver[Integer.parseInt(deviceIndex)]).hideKeyboard();
//            }
//        } catch (NoSuchElementException e) {
//            System.out.println("No keyboard: " + e.getMessage());
//        }
//
//    }
    private static int getX(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);

        return driver[index].findElement(by).getRect().x;
    }
    private static int getX(String deviceIndex, WebElement element) {
        int index = Integer.parseInt(deviceIndex);

        return element.getRect().x;
    }
    private static int getY(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);

        return driver[index].findElement(by).getRect().y;
    }
    private static int getY(String deviceIndex, WebElement element) {
        int index = Integer.parseInt(deviceIndex);

        return element.getRect().y;
    }
    private static int getWidth(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);

        return driver[index].findElement(by).getSize().width;
    }
    private static int getWidth(String deviceIndex, WebElement element) {
        int index = Integer.parseInt(deviceIndex);

        return element.getSize().width;
    }
    private static int getHeight(String deviceIndex, By by) {
        int index = Integer.parseInt(deviceIndex);

        return driver[index].findElement(by).getSize().height;
    }
    private static int getHeight(String deviceIndex, WebElement element) {
        int index = Integer.parseInt(deviceIndex);

        return element.getSize().height;
    }
    public static void tapAtAPosition(String deviceIndex, By by) {
        int x = ElementActions.getX(deviceIndex, by);
        int y = ElementActions.getY(deviceIndex, by);
        int width = ElementActions.getWidth(deviceIndex, by);
        int height = ElementActions.getHeight(deviceIndex, by);

        tapAtAPosition(deviceIndex, x + (width / 2), y + (height / 2));
    }
    public static void tapAtAPosition(String deviceIndex, WebElement element) {
        int x = ElementActions.getX(deviceIndex, element);
        int y = ElementActions.getY(deviceIndex, element);
        int width = ElementActions.getWidth(deviceIndex, element);
        int height = ElementActions.getHeight(deviceIndex, element);

        tapAtAPosition(deviceIndex, x + (width / 2), y + (height / 2));
    }
    public static void clickByPosition(String deviceIndex, WebElement element) {
        int x = element.getRect().x;
        int y = element.getRect().y;
        int width = element.getRect().width;
        int height = element.getRect().height;

        tapAtAPosition(deviceIndex, x + (width / 2), y + (height / 2));
    }
    // Check for element status
    public static boolean isElementDisappeared(String deviceIndex, By by, int timeLimitInSeconds) {
        int index = Integer.parseInt(deviceIndex);
        driver[index].manage().timeouts().implicitlyWait(Duration.ofSeconds(timeLimitInSeconds));
        try {
            WebDriverWait wait = new WebDriverWait(driver[index], Duration.ofSeconds(timeLimitInSeconds));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static void dragElement(String deviceIndex, By by, int fromX, int fromY, int toX, int toY) {
        int index = Integer.parseInt(deviceIndex);
        JavascriptExecutor js = (JavascriptExecutor) driver[index];
        Map<String, Object> scrollObject = new HashMap<>();
        scrollObject.put("duration", 0);
        scrollObject.put("fromX", fromX);
        scrollObject.put("fromY", fromY);
        scrollObject.put("toX", toX);
        scrollObject.put("toY", toY);
        scrollObject.put("element", ((RemoteWebElement) ElementActions.captureElement(deviceIndex, by)).getId());

        js.executeScript("mobile: dragFromToForDuration", scrollObject);
    }
    public static String getText(String deviceIndex, By by) {
        waitElementDisplayed(deviceIndex, by);
        return driver[Integer.parseInt(deviceIndex)].findElement(by).getText();
    }
    public static void checkTextboxValue(String deviceIndex, By by, String textboxName, String expectedValue) {
        int index = Integer.parseInt(deviceIndex);
        String actualValue = driver[index].findElement(by).getText();
        if (!actualValue.equals(expectedValue)) {
//            result[index] = false;
//            message[index] += formatMessage("Check '" + textboxName + "' textbox value:",
//                    actualValue, expectedValue);
        }
    }
    public String getAttributeValue(String deviceIndex, By by, boolean waitForElement, String attributeName) {
        String attributeValue = "";
        if (waitForElement)
            attributeValue = captureElement(deviceIndex, by).getAttribute(attributeName).toString();
        return attributeValue;
    }

    public static String getAttributeValue(String deviceIndex, By by, String attributeName) {
        String result = captureElement(deviceIndex, by).getAttribute(attributeName);

        if (result == null) {
            result = "";
        }
        return result;
    }

    public static void checkAttributeValue(String deviceIndex, By by, String elementName, String attributeName, String expectedValue) {
        int index = Integer.parseInt(deviceIndex);
        String actualValue = getAttributeValue(deviceIndex, by, attributeName);
//        if (!actualValue.equals(expectedValue)) {
//            result[index] = false;
//            message[index] += formatMessage("Check element '" + elementName + "'s " + attributeName + "' value:",
//                    actualValue, expectedValue);
//        }
    }
    public static void pressKey(String deviceIndex, By by, String EnterOrTab) {
        int index = Integer.parseInt(deviceIndex);
        switch (EnterOrTab) {
            case "Tab": driver[index].findElement(by).sendKeys(Keys.TAB);
            case "Enter":
                if (System.getProperty("os.name").contains("Mac")) {
                    driver[index].findElement(by).sendKeys(Keys.RETURN);
                }
                else {
                    driver[index].findElement(by).sendKeys(Keys.ENTER);
                }
        }

    }
//    public static void allowNotification(String deviceIndex) {
//        click(deviceIndex, SysObjects.AllowNotiBtn[platformIndex]);
//    }
//    public static void allowLocation(String deviceIndex) {
//        click(deviceIndex, SysObjects.AllowWhileUsingAppLocBtn[platformIndex]);
//    }
//    public static void navigateToUrl(String url) {
//        webDriver.navigate().to(url);
//        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(GlobalData.shortWait));
//    }
}
