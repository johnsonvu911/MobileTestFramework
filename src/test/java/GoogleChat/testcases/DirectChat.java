package GoogleChat.testcases;

import GoogleChat.screens.BottomNavigator;
import core.base.TestBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DirectChat extends TestBase {
    public DirectChat(){
        super();
    }

    @Test(description = "Start new direct chat", groups = {"regression, e2e"})
    @Parameters({"device"})
    public void StartNewDirectChat(@Optional(testDeviceIndex) String device) {
        BottomNavigator.clickDirectChatButton(device);
    }
}
