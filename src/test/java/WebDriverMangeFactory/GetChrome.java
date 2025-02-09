package WebDriverMangeFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Listeners;
import reuse.AllureLog4jListener;

import static java.lang.invoke.MethodHandles.lookup;

@Listeners({AllureLog4jListener.class, AllureTestNg.class})
public class GetChrome extends GetWebDriver{
    public static WebDriver setupChromeDriver(String mode) {
        Logger log = LogManager.getLogger(lookup().lookupClass());
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Check if the mode contains any supported mode part and apply the corresponding Chrome option
        if (mode != null) {
            for (String supportedMode : MODES) {
                if (mode.toLowerCase().contains(supportedMode)) {
                    options.addArguments(getArgumentForMode(supportedMode));
                    log.info("Launching Chrome browser with mode: " + supportedMode);
                }
            }
        }else  {
            log.info("Launching Chrome browser in default mode (no special modes enabled).");
        }
        // Create and return the WebDriver
        WebDriver driver = new ChromeDriver(options);
        maximizeWindow(driver);
        setImplicitWait(driver);
        return driver;
    }

}
