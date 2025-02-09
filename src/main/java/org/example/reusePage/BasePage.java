package org.example.reusePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.invoke.MethodHandles.lookup;


public class BasePage {

    public static final Logger log = LogManager.getLogger(lookup().lookupClass());
    protected static WebDriver BasePageDriver;


     WebDriverWait wait = new WebDriverWait(BasePageDriver, Duration.ofSeconds(10));

    public BasePage(WebDriver mydriver){
        BasePageDriver=mydriver;
    }

    private final By Page_headline=By.xpath("//*[@id='bodyContainer']/h2");
    private final By searchbar=By.xpath("//input[@placeholder='Search']");

    public void testSearchField(By searchField, By resultField, String searchValue) throws InterruptedException {
        WebElement searchInput = BasePageDriver.findElement(searchField);
        searchInput.clear();
        searchInput.sendKeys(searchValue);
        // Verify result
        Thread.sleep(2000);
        WebElement resultElement = BasePageDriver.findElement(resultField);
        String resultText = resultElement.getText();
        Assert.assertEquals(resultText,searchValue);
        searchInput.clear();
        log.info("Search Value: {} | Result: {}", searchValue, resultText);
    }
    public void enterSearch(String search, By location){
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchbar));
        BasePageDriver.findElement(searchbar).clear();
        BasePageDriver.findElement(searchbar).sendKeys(search);
        BasePageDriver.findElement(location).click();
        log.info("Search for {} in the search bar.",search);
    }

    public void navigateBack(WebDriver Driver) throws InterruptedException {
        Driver.navigate().back();
        Thread.sleep(2000);
    }

    public void Assertion_page_Title_Name(String Page_Name){
        wait.until(ExpectedConditions.visibilityOfElementLocated(Page_headline));
        String title =  BasePageDriver.findElement(Page_headline).getText();
        Assert.assertEquals(title,Page_Name);
        log.info("you are in the {} Page and this is correct", Page_Name);
        System.out.println("you are in the "+Page_Name+" Page and this is correct");
    }
    public void assertFileNameSaved(String exportTypeDownload, String filename) {
        // Determine the expected file extension based on export type
        String exportType;
        if (exportTypeDownload.equals("Excel")) {
            exportType = ".xlsx";
        } else if (exportTypeDownload.equals("CSV")) {
            exportType = ".csv";
        } else {
            System.out.println("Unsupported export type: " + exportTypeDownload);
            return;
        }

        // Format the current date (without time) to append to the filename
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter);
        String expectedFileName = filename + date; // Append the export type at the end

        // Get the user home directory and downloads path
        String userHome = System.getProperty("user.home");
        String downloadedFileDir = userHome + "/Downloads";

        // Create a File object for the downloaded file directory
        File downloadDir = new File(downloadedFileDir);

        // Ensure the directory exists
        if (!downloadDir.exists() || !downloadDir.isDirectory()) {
            System.out.println("Download directory is not valid or doesn't exist.");
            return;
        }

        // Filter files in the directory that match the extension (CSV or Excel)
        List<File> files = List.of(Objects.requireNonNull(downloadDir.listFiles(file -> file.getName().endsWith(exportType))));

        if (files.isEmpty()) {
            System.out.println("No files found with the specified extension.");
        } else {
            boolean fileFound = false; // Flag to track if the file is found
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(exportType)) {
                    // Extract the base file name and today's date from the file name
                    String baseFileName = fileName.substring(0, fileName.indexOf('_') + 10); // Up to "_yyyy-MM-dd"

                    // Check if the file name contains the base filename and today's date
                    System.out.println("Expected result: " + expectedFileName + " and the Actual result: " + fileName);
                    if (fileName.contains(baseFileName)) {
                        fileFound = true;
                        // Assert that the file name contains the expected pattern
                        Assert.assertTrue(fileName.contains(expectedFileName),"File name does not match the expected pattern.");
                        System.out.println("\n**Assertion Passed, The file is saved correctly with name: '" + fileName + "'");
                        break;
                    }
                }
            }

            // If the file is not found, log the failure
            if (!fileFound) {
                System.out.println("No file found with the expected name: " + expectedFileName);
            }
        }
    }
    public String returnPrintType(String printType) {

        return switch (printType.toLowerCase()) {
            case "xls" -> "XLS";
            case "xlsx" -> "XLSX";
            default -> "PDF";
        };


    }
}



















/*
* wait
* implict wait
* explict wait
* thread
*
*
* */





























