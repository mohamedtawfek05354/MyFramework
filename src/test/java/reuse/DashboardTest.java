package reuse;

import org.example.reusePage.DashboardPage;
import org.testng.annotations.Test;

public class DashboardTest extends BaseTest {
    DashboardPage DP;
    @Test
    public void Dashboard() throws InterruptedException {
        DP = new DashboardPage(BaseTestDriver);
        DP.enterDashboardicon();
        DP.Assert_page_Tiltle();
        Thread.sleep(1000);
        DP.entersearch("Dashboard");
        Thread.sleep(1000);
        DP.enterbranchno();
        DP.enterDateto("13/07/2024");
        Thread.sleep(1000);
        DP.enterDatefrom("09/07/2024");
        Thread.sleep(1000);
        DP.clickgetserverdata();
        Thread.sleep(1000);

    }
}
