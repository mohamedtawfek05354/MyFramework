package WebDriverMangeFactory;

import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;
import reuse.AllureLog4jListener;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import static java.lang.invoke.MethodHandles.lookup;

@Listeners({AllureLog4jListener.class, AllureTestNg.class})
public class GetWebDriver {
    // List of all possible modes with their respective ChromeOptions arguments
    public static String[] MODES = {
            "headless",            // --headless
            "incognito",           // --incognito
            "fullscreen",          // --start-fullscreen
            "disable-extensions",  // --disable-extensions
            "disable-gpu",         // --disable-gpu (useful for headless mode)
            "start-maximized",     // --start-maximized
            "no-sandbox",          // --no-sandbox
            "remote-debugging-port", // --remote-debugging-port=9222
            "enable-logging",      // --enable-logging
            "disable-dev-shm-usage" // --disable-dev-shm-usage (useful for Docker environments)
    };
    public static int timeout = 10; //to set ImplicitWait for the driver
    private static WebDriver webDriver;
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());
    public static WebDriver launchBrowser(String browser,String mode) throws Exception {
        initialize_Logging();
        switch (browser.toLowerCase()) {
            case "chrome":
                webDriver=GetChrome.setupChromeDriver(mode);
                break;
            case "firefox":
                webDriver=GetFirefox.setupFirefoxDriver(mode);
                break;
            case "edge":
                webDriver=GetEdge.setupEdgeDriver(mode);
                break;
            default:
                log.warn("Unknown browser specified: '{}'", browser);
                throw new Exception("Unknown browser specified.");
        }
        log.info("Website loaded successfully in {} mode for {}", browser.contains("headless") ? "headless" : "normal", browser);
        return webDriver;
    }
    public static void maximizeWindow(WebDriver Driver) {
        Driver.manage().window().maximize();
        log.debug("Browser window maximized");
    }

    public static void setImplicitWait(WebDriver Driver) {
        Driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        log.debug("Implicit wait of {} seconds set for all elements", timeout);
    }
    private static void initialize_Logging() {
        String logFilePath = "./logs/application.log";
        try (FileWriter writer = new FileWriter(logFilePath, false)) {
            writer.write("");
            log.info("Log file '{}' cleaned successfully.", logFilePath);
        } catch (IOException e) {
            log.error("Failed to clean the log file '{}'. Error: {}", logFilePath, e.getMessage(), e);
        }
    }
    public static String getArgumentForMode(String mode) {
        switch (mode) {
            case "headless":
            case "headless mode":
                return "--headless";
            case "incognito":
            case "incognito mode":
            case "inco":
                return "--incognito";
            case "fullscreen":
                return "--start-fullscreen";
            case "disable-extensions":
                return "--disable-extensions";
            case "disable-gpu":
                return "--disable-gpu";
            case "start-maximized":
                return "--start-maximized";
            case "no-sandbox":
                return "--no-sandbox";
            case "remote-debugging-port":
            case "remote":
                return "--remote-debugging-port=9222";
            case "enable-logging":
                return "--enable-logging";
            case "disable-dev-shm-usage":
                return "--disable-dev-shm-usage";
            default:
                return "";
        }
    }
}
