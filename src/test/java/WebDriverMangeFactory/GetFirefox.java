package WebDriverMangeFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.Listeners;
import reuse.AllureLog4jListener;

import static java.lang.invoke.MethodHandles.lookup;

@Listeners({AllureLog4jListener.class, AllureTestNg.class})
public class GetFirefox extends GetWebDriver{
    public static WebDriver setupFirefoxDriver(String mode) {
        Logger log = LogManager.getLogger(lookup().lookupClass());

        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        // Check if the mode contains any supported mode part and apply the corresponding Chrome option
        if (mode != null) {
            for (String supportedMode : MODES) {
                if (mode.toLowerCase().contains(supportedMode)) {
                    options.addArguments(getArgumentForMode(supportedMode));
                    log.info("Launching Firefox browser with mode: " + supportedMode);
                }
            }
        }else  {
            log.info("Launching Firefox browser in default mode (no special modes enabled).");
        }
        WebDriver driver = new FirefoxDriver(options);
        maximizeWindow(driver);
        setImplicitWait(driver);
        return driver;
    }
}
