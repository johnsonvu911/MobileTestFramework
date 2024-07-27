package GoogleChat.testdata;

import java.util.List;
import java.util.Random;

public class Testdata {
    public static final String Email1 = "johnsonvu911@mailinator.com";
    public static final String Email2 = "liquidvoon9986@mailinator.com";
    public static final String Password1 = "abc12345";
    public static final String Password2 = "voon10106";
    public static final String DunitNowId = "1115266766"; // Success transfer
    public static final String InvalidDunitNowId = "09876543";
    public static final String IncorrectFormatDunitNowId = "12345";
    public static final String DunitNowIdToBeFail = "123456789"; // Fail transfer
    public static List<Integer> phoneNumber = List.of(1, 1, 1, 5, 2, 6, 6, 7, 6, 6); // For Android only.
    public static String SendAmount = String.format("10.%02d", new Random().nextInt(100));
    public static final String InvalidSendAmount = "9.99";
    public static final String InsufficientSendAmount = "980980980980.99";
    public static final String ExceedDailyTransferLimit = "1000.99";
    public static final String MinAmountLimitErrorMsg = "Minimum transfer amount $ 10.00";
    public static final String DailyTransferLimitMsg = "Daily transfer limit exceeded.";
    public static final String ExceedBalanceMsg = "Your Liquid Account Balance: $ %s";
    public static final String BundleId = "com.korvac.liquidDev.dev";
    public static final String AppPackage = "com.korvac.liquid.dev";
    public static final String AppName = "Liquid Pay QA";
    public static final String PlatformVersion = "17.0";
    public static final String DeviceName = "iPhone 15 Pro Max";
    public static final String Udid_Iphone15ProMax = "61E3507F-39B0-4457-B9ED-654F1B5517A5";
    public static final String appActivity = "com.korvac.liquid.presentation.intro.SplashActivityV2";
    public static String duitNowIdLbl = "DuitNow ID: +60%s";
    public static final String InitiatedTransferToLbl = "Initiated transfer to";
    public static final String InfoMsg = "Please note, once the transfer is successful, it will appear in the transaction history. Should the funds not be credited to the recipient’s account, you will be refunded within 24 hours.";
    public static final String TransferNotes = "Hello! This is test transfer.";
    public static final String TransferFailed = "Transfer Failed";
    public enum STATUS {
        Sent, Failed
    }
    public enum PLATFORM {
        iOS, Android
    }
    public static final String InvalidDuitNowIDTitle = "Invalid DuitNow ID";
    public static final String InvalidDuitNowIDMsg = "You won’t be able to make a transfer \nto your friend who doesn’t have a \nvalid DuitNow ID";
    public static final String IncorrectDuitNowIdErrMsg = "Incorrect format. The DuitNow ID is in the format of a phone number";

}
