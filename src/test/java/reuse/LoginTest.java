package reuse;

import org.example.reusePage.LoginPage;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    LoginPage lo;

    @Test
    public void login () throws InterruptedException {
        lo = new LoginPage(BaseTestDriver);
        lo.enterEmail("Test2@test.com");
        lo.enterPassword("user@123");
        lo.clickloginButton();
        lo.Select_Branch("5");
//        lo.selectCashier("2");
//        lo.selectStore("3");
        lo.selection();
      
    }
}
