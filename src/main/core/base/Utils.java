package base;

import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;


public class Utils extends Initiatives {
    public static HashMap readJsonFile(String filePath) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        Gson gson = new Gson();
        return gson.fromJson(bufferedReader, HashMap.class);
    }

    public static List<Map<String, String>> readXMLFile(String filePath, String tagName) {
        List<Map<String, String>> info = new ArrayList<Map<String, String>>();
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList deviceList = doc.getElementsByTagName(tagName);

            for (int i = 0; i < deviceList.getLength(); i++) {
                Node device = deviceList.item(i);
                if (device.getNodeType() == Node.ELEMENT_NODE) {
                    Element deviceNode = (Element) device;
                    NodeList deviceInfo = deviceNode.getChildNodes();
                    Map<String, String> phone_info = new HashMap<String, String>();
                    for (int j = 0; j < deviceInfo.getLength(); j++) {
                        Node deviceValue = deviceInfo.item(j);

                        if (deviceValue.getNodeType() == Node.ELEMENT_NODE) {
                            Element valueNode = (Element) deviceValue;
                            phone_info.put(valueNode.getTagName(), valueNode.getTextContent());
                        }
                    }
                    info.add(phone_info);
                }
            }
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }
    public static String getCurrentDatetime() {
        return LocalDateTime.now().toString();
    }

    public static Number generateRandomNumber() {
        return new Random().nextInt(999999999 + 1);
    }
    public static String formatMessage(String message, String actualResult, String expectedResult) {
        String text = "";
        text += String.format("%s\r\n", message);
        text += String.format("- Actual  : %s\r\n", actualResult);
        text += String.format("- Expected: %s\r\n", expectedResult);
        text += "\r\n";
        return text;
    }
    public static String getPropertyValue(String resourceFileName, String keyName) throws IOException, NullPointerException {
        Properties properties = new Properties();
        ClassLoader classLoader = Utils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFileName + ".properties");
        properties.load(inputStream);
        return properties.getProperty(keyName);
    }
    public static Map<String, String> getPropertyValues(String resourceFileName) {
        Map<String, String> values;
        Properties properties;
        try {
            properties = new Properties();
            // Declare a ClassLoader object and assign it the class loader of the Utils class
            ClassLoader classLoader = Utils.class.getClassLoader();
            // Declare an InputStream object and assign it the resource stream of the properties file
            InputStream inputStream = classLoader.getResourceAsStream(resourceFileName + ".properties");
            // Load the properties from the input stream
            properties.load(inputStream);
            // Use the stringPropertyNames method to get the Set of property names
            Set<String> keys = properties.stringPropertyNames();
            values = new HashMap<>();
            // Use a loop to iterate over the property names that you want to add to the map
            for (String key : keys) {
                String value = properties.getProperty(key);
                values.put(key, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return values;
    }
    public static int getDeviceIndex() {
        for (int i = 0; i < device.size(); i++) {
            if (device.get(i).get("profilename").equals(profileName)) {
                return i;
            }
        }
        return -1; // if no match is found
    }
    public static String getAbsolutePath(String pathFromContentRoot) {
        return new File(pathFromContentRoot).getAbsoluteFile().toString();
    }
    public static String getPathToFileInResources(String url) throws NullPointerException {
        URL path = Utils.class.getClassLoader().getResource(url);
        return Objects.requireNonNull(path).getPath();
    }
    public static double roundDoubleNumber(double number, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(number); // Convert it to a BigDecimal
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP); // Round it to two decimal places
        return bd.doubleValue(); // Convert it back to a double
    }
    public static void threadSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
    public static void saveTextToClipboard(String text) {
        StringSelection selection = new StringSelection (text);
        Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        clipboard.setContents (selection, selection);
    }
    public static String getTextFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);
        String textFromClipboard = "";
        if (transferable != null && transferable.isDataFlavorSupported (DataFlavor.stringFlavor)) {
            try {
                textFromClipboard = (String) transferable.getTransferData (DataFlavor.stringFlavor);
                logger.info(textFromClipboard);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return textFromClipboard;
    }
    public static List<String> stringToList(String number) {
        char[] chars = number.toCharArray();
        List<String> list = new ArrayList<>();
        for (char c : chars) {
            list.add(String.valueOf(c));
        }
        return list;
    }
}