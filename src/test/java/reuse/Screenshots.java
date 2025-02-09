package reuse;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class Screenshots {
    public static File takeshots(WebDriver driver,String screenshotpath) throws IOException
    {
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrshot=((TakesScreenshot)driver );
        //Call getScreenshotAs method to create image file
        File scrfile=scrshot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile=new File(screenshotpath);
        //Copy file at destination
        FileHandler.copy(scrfile,DestFile);
        return DestFile;

    }


}
