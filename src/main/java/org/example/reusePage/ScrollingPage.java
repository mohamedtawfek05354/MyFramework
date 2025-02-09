package org.example.reusePage;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.invoke.MethodHandles.lookup;

public class ScrollingPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(lookup().lookupClass());
    public ScrollingPage(WebDriver mydriver) {
        super(mydriver);

    }
    public static void ScrollToElement(By locator){
        // Create an Actions object
        Actions actions = new Actions(BasePageDriver);
        // Move to the element to scroll it into view
        WebElement target = BasePageDriver.findElement(locator);
        actions.moveToElement(target).perform();
        log.info("The scroll to the element located in {} is working fine using Actions",locator);
    }
    public static void Scroll(int x, int y){
        JavascriptExecutor js = (JavascriptExecutor) BasePageDriver;
        js.executeScript("window.scrollBy("+x+","+y+")", "");
        log.info("scroll the window by ({},{})", x, y);
    }
    public static void ScrollBottom() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_END);
        robot.keyRelease(KeyEvent.VK_END);
        robot.keyRelease(KeyEvent.VK_END);
        log.info("Scroll to the Bottom of the page");
    }
    public static void ScrollUp() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        log.info("Scroll to the UP of the page");

    }
    public static void ScrollToElement(WebElement elementLocator) throws InterruptedException {
        // Create an Actions instance
        Actions actions = new Actions(BasePageDriver);
        // Move to the scrollable element and press the RIGHT arrow key to scroll right
        actions.moveToElement(elementLocator).sendKeys(Keys.RIGHT).perform();
        actions.sendKeys(Keys.RIGHT).perform();
        Thread.sleep(2000);
        actions.sendKeys(Keys.RIGHT).perform();
        actions.sendKeys(Keys.RIGHT).perform();
        log.info("The scroll to the element {} is working fine using Actions",elementLocator);

    }
    public static void ScrollRight() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        log.info("Scroll to the Right of the page");

    }
    public static void ScrollLeft() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
        log.info("Scroll to the Left of the page");

    }
}
