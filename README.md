# 📱 Mobile Test Automation Framework

## 🚀 Purpose of This Repository

This is a mobile automation testing framework for Android/iOS apps built with **Java**. It's designed to be **modular**, scalable, and easy to maintain. The framework supports integration with **CI/CD pipelines** and aligns well with **Agile teams**.

## ⚙️ Installation & Execution

### Requirements:

* Java 11+
* Maven 3.6+
* Appium Server (installed and running)
* Android SDK / Xcode (depending on target platform)
* Real device or emulator/simulator (Android/iOS)
* IDE: IntelliJ IDEA or Eclipse with Maven plugin

### How to Run Tests:

```bash
# Run all tests
mvn clean test

# Run tests with a specific device profile
mvn clean test -Dprofile=androidPixel
```

## 📁 Project Structure Overview

```
src/
├── main/
│   ├── java/core/           # Core framework source code
│   │   ├── base/            # Base classes: TestBase, ElementActions, Reporter, etc.
│   │   ├── helpers/         # Utility classes (AppHelper, CommandExecutor, etc.)
│   │   └── datasets/        # Static app data (e.g., AppInfo)
│   └── resources/           # Configuration, device profiles, logging
│
├── test/
│   └── java/GoogleChat/     # App-specific test scripts
│       ├── screens/         # Page Object Models for UI screens
│       └── testcases/       # Test scenarios
```

## 🧠 Key Classes & Functions

| File/Class                   | Description                                                    |
| ---------------------------- | -------------------------------------------------------------- |
| `TestBase.java`              | Initializes the Appium driver and sets up the test environment |
| `ElementActions.java`        | UI interaction helpers (click, type, swipe, etc.)              |
| `Reporter.java`              | Handles custom test execution reporting                        |
| `RetryAnalyzer.java`         | Automatically retries failed tests                             |
| `AppHelper.java`             | App-level utility methods                                      |
| `HttpRequester.java`         |	Provides reusable HTTP client utilities to send GET, POST, PUT requests for API interactions |
| `AppConfig.properties`       | Global configuration file                                      |
| `.json` files in `profiles/` | Device-specific configurations                                 |
| `screens/*.java`             | Page Object Models representing app screens                    |
| `testcases/*.java`           | Actual test scenarios and logic                                |

## 🧪 Writing and Running Tests

1. Define the screen elements in `screens/` using the Page Object Model pattern
2. Create test logic in the corresponding class in `testcases/`
3. Run tests via Maven using the desired device profile

## 📓 Technical Notes

* **Dependencies**: Managed via `pom.xml` (Appium, TestNG, log4j2, Gson, etc.)
* **CI/CD**: Easily integrates with Jenkins or GitHub Actions using Maven CLI
* **Retry & Logging**: Supports automatic retry of failed tests and detailed logs via `log4j2`
* **Device Profiles**: Allows multi-device test execution via JSON configuration
* **Reporting**: Extensible for reporting tools like ExtentReports or Allure
* **API Testing Support**: The HttpRequester utility enables simple HTTP calls (GET, POST, PUT) using Java 11's HttpClient, supporting use cases like pre-test setup, backend validation, or hybrid API + UI tests.



## 🛠 Suggested Roadmap (Optional Enhancements)

* [ ] Add iOS support
* [ ] Integrate ExtentReports or Allure for rich reporting
* [ ] Enable parallel test execution
* [ ] Add REST API test capabilities using REST-assured
