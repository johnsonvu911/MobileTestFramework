package helpers;

import base.Initiatives;
import org.testng.util.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class AppInstaller extends Initiatives {
    public static void installIOSApp(String pathToAppFileInResources, String UDID, String deviceName, String appName) {
        // The path to the .app file of the iOS app
        URL appUrl = AppInstaller.class.getClassLoader().getResource(pathToAppFileInResources);
        String appPath = Objects.requireNonNull(appUrl).getPath();
        // The command to check if the app is installed
        String checkCommand = String.format("xcrun simctl listapps \"%s\" | grep \"%s\"", deviceName, appName);
        // The command to install the app
        String installCommand = String.format("xcrun simctl install %s %s", UDID, appPath);
        try {
            // Create a ProcessBuilder instance with the shell and its arguments
            ProcessBuilder checkBuilder = new ProcessBuilder("/bin/bash", "-c", checkCommand);
            // Start the process and get a Process instance
            Process checkProcess = checkBuilder.start();
            System.out.println("Executing check command: " + checkBuilder.command());
            // Execute the check command and read the output
            BufferedReader checkReader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
            String checkOutput = checkReader.readLine();
            checkReader.close();
            checkProcess.waitFor();

            // If the output is null or empty, the app is not installed
            if (Strings.isNullOrEmpty(checkOutput)) {
                // Execute the install command and read the output
                ProcessBuilder installBuilder = new ProcessBuilder("/bin/zsh", "-c", installCommand);
                Process installProcess = installBuilder.start();
                System.out.println("Executing install command: " + installBuilder.command());
                BufferedReader installReader = new BufferedReader(new InputStreamReader(installProcess.getInputStream()));
                String installOutput = installReader.readLine();
                installReader.close();
                installProcess.waitFor();

                // If the output is not null or empty, the installation was successful
                if (Strings.isNullOrEmpty(installOutput)) {
                    System.out.println("The app is installed successfully.");
                } else {
                    System.out.println("The app installation failed.");
                }
            } else if (checkOutput.contains(appName)) {
                System.out.println("The app is already installed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
