package core.helpers;

import core.base.Initiatives;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor extends Initiatives {
    public static String executeCommand (String command) {
        String output = "";
        try {
            // Create a ProcessBuilder instance with the shell and its arguments
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            // Start the process and get a Process instance
            Process process = builder.start();
            logger.info("Executing command: {}", builder.command());
            // Execute the check command and read the output
            BufferedReader checkReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = checkReader.readLine();
            checkReader.close();
            process.waitFor(); // Wait for the process to finish
        } catch (IOException | InterruptedException e) {
            logger.error("Exception error with command execution: {}", e.getMessage());
        }
        return output;
    }
    public static void executeScript (String pathToScriptFile, @Nullable String arg) {
        try {
            Process process = Runtime.getRuntime().exec("/bin/bash " + pathToScriptFile + arg);
            process.waitFor(); // Wait for the process to finish
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("{}\n", line);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
