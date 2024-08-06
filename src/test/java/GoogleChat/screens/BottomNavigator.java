package GoogleChat.screens;

import core.base.ElementActions;
import core.base.TestBase;

public class BottomNavigator extends TestBase {
    public static final String[] DirectChatBtn = {
            "",
            "//android.view.View[@content-desc=\"Direct messages. \"]"
    };
    public static final String[] HomeBtn = {
            "",
            "//android.view.View[@content-desc=\"Home. \"]"
    };

    public static void clickDirectChatButton(String device) {
        ElementActions.click(device, DirectChatBtn[platformIndex]);
    }

}
