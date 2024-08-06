## Commands to get UDID, App_Package and App_Activity
These parameters are required for starting a specific app.
```
$ adb devices 
Output: 
List of devices     attached
<UDID>	            device

$ adb shell

// MacOS
$ dumpsys window | grep -E 'mCurrentFocus'

// Windows
dumpsys window windows | grep -E 'mCurrentFocus|mFocusedApp'

Output: {<random digits> u0 <APP_PACKAGE>/<APP_ACTIVITY>}
```
## Prerequisites 
**Install Homebrew, Carthage, and Node.js**
````
$ /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
$ brew install carthage
$ brew install node
````

**Install Java JDK and Set environment variable path by adding the following two lines into .bash_profile or in your desired shell**
````
export JAVA_HOME="copied-path-to-JDK-directory"
export PATH=$JAVA_HOME/bin:$PATH
````

**Install Appium with npm**
````
npm install -g appium
````

Optionally, you're recommended to install the Appium Doctor in addition to Appium. It will help you fix issues with Appium and iOS settings easier.
````
npm install -g appium-doctor
````

**Install Xcode and Xcode command-line tools**

Download and install Xcode: https://developer.apple.com/xcode/

**Install Xcode command-line tools**
````
$ xcode-select --install
````

**Install uiautomator2 driver for Android platform** 
````
$ appium driver install uiautomator2 
````

**Install WebDriverAgent**

WebDriverAgent is a WebDriver version for iOS. It serves for controlling iOS devices and apps remotely. You can find it in the following directories:

If you installed Appium Desktop: /Applications/Appium.app/Contents/Resources/app/node_modules/appium/node_modules/appium-webdriver

If you installed Appium via Node.js: /usr/local/lib/node_modules/appium/node_modules/appium-webdriveragent

To install:
````
mkdir -p Resources/WebDriverAgent.bundle
./Scripts/bootstrap.sh -d
````

Open the WebDriverAgent.xcodeproj project in Xcode (you can find the project in one of the directories specified above).

For the WebDriverAgentLib and WebDriverAgentRunner targets, go to the General tab and select the Automatically manage signing check box there, and then select your development team.

It’s possible that Xcode will fail to create a provisioning profile for the WebDriverAgentRunner target. See below for information on how to fix this.

See fixing instructions: 
````
The error text looks like this —

Failed to create provisioning profile...
No profile for '...' was found

To fix this issue, try changing the bundle ID for the target you use:

Go to the Build Settings tab page.

Change the Product Bundle Identifier value from —

com.facebook.WebDriverAgentRunner

– to –

com.facebook.WebDriverAgentRunnerXXX,

where XXX is an arbitrary string.
````


Build the project to verify that everything works fine:
````
xcodebuild -project WebDriverAgent.xcodeproj -scheme WebDriverAgentRunner -destination 'id=udid' test
````

If the build fails because of the issues with the provisioning profile (the error message has the word “Provision”), the error message will mention the arguments to be specified in the command line. Modify the command line to make the build pass successfully.

Optionally, run the Appium Doctor to check if everything is configured properly. The Doctor will provide detailed information on issues, if any:
````
$ appium-doctor
````

***Install Appium Gestures plugin***

Installation - Server

Install the plugin using Appium's plugin CLI, either as a named plugin or via NPM:
````
$ appium plugin install --source=npm appium-gestures-plugin
````

The plugin will not be active unless turned on when invoking the Appium server:
````
$ appium --use-plugins=gestures
````