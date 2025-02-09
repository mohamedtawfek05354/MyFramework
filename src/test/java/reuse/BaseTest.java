package reuse;

import WebDriverMangeFactory.GetWebDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.lang.invoke.MethodHandles.lookup;

@Listeners({AllureLog4jListener.class, AllureTestNg.class})
public class BaseTest extends GetWebDriver{
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());
    public static WebDriver BaseTestDriver;
    public ScreenRecorderUtil recorderUtil;
    private String outputPathCompare;
    private String outputPathContain;
    private String outputPathNumbers;
    public static String DB_Name;
    public static String UserName_SQL;
    public static String Pass_SQL;
    public static String SQLIP_portal;
    public static String MYSQL_Name;
    public static String UserName_MySQL;
    public static String Pass_MySQL;
    public static String URL_MySQL;
    public static String MySQL_Vendor;
    public static String IDFrom;
    public static String IDTo;
    public static String Config_From;
    public static String Config_To;
    public static String Config_DB_Name;
    public static String Config_UserName_SQL;
    public static String Config_Pass_SQL;
    public static String Config_UserName_MySQL;
    public static String Config_SQL_IP_portal;
    public static String Config_Pass_MySQL;
    public static String Config_URL_MySQL;
    public static String Config_MySQL_Name;
    public static String browser;
    public static String url;
    public static String mode;

    //-----------------------------------------------------------------------------------------------------------------------------------------//
    @BeforeSuite
    public void SetUp() throws IOException {
        // enter file path to delete it from downloads section
        Clear_Downloads("file");
        log.info("Starting test Setup execution");
        ConfigLoader configLoader = new ConfigLoader("./src/main/resources/config.properties");
        ConfigLoader DBConfig = new ConfigLoader("./src/main/resources/DB.properties");
        Config_From=DBConfig.getProperty("Config_From");
        Config_To=DBConfig.getProperty("Config_To");
        Config_DB_Name=DBConfig.getProperty("DB_Name");
        Config_Pass_SQL=DBConfig.getProperty("Pass_SQL");
        Config_UserName_SQL=DBConfig.getProperty("UserName_SQL");
        Config_SQL_IP_portal=DBConfig.getProperty("SQL_IP_portal");
        Config_URL_MySQL=DBConfig.getProperty("URL_MySQL");
        Config_Pass_MySQL=DBConfig.getProperty("Pass_MySQL");
        Config_UserName_MySQL=DBConfig.getProperty("UserName_MySQL");
        Config_MySQL_Name=DBConfig.getProperty("MySQL_Name");
        IDFrom = System.getProperty("IDFrom", Config_From);
        IDTo = System.getProperty("IDTo", Config_To);
        browser=configLoader.getProperty("browser");
        mode=configLoader.getProperty("mode");
        url=configLoader.getProperty("url");
        MySQL_Vendor=DBConfig.getProperty("VendorId");
        DB_Name = System.getProperty("DB_name", Config_DB_Name);
        SQLIP_portal=System.getProperty("SQLIP_Portal",Config_SQL_IP_portal);
        UserName_SQL = System.getProperty("SQL_UserName", Config_UserName_SQL);
        Pass_SQL = System.getProperty("SQL_Pass", Config_Pass_SQL);
        MYSQL_Name = System.getProperty("MySQL_Name",Config_MySQL_Name);
        UserName_MySQL = System.getProperty("MySQL_UserName", Config_UserName_MySQL);
        Pass_MySQL = System.getProperty("MySQL_Pass", Config_Pass_MySQL);
        URL_MySQL = System.getProperty("MySQL_URL", Config_URL_MySQL);

        System.out.println("DB_Name: " + System.getProperty("DB_name"));
        System.out.println("SQL_UserName: " + System.getProperty("SQL_UserName"));
        System.out.println("SQL_Pass: " + System.getProperty("SQL_Pass"));
        System.out.println("MySQL_Name: " + System.getProperty("MySQL_Name"));
        System.out.println("MySQL_UserName: " + System.getProperty("MySQL_UserName"));
        System.out.println("MySQL_Pass: " + System.getProperty("MySQL_Pass"));
        System.out.println("MySQL_URL: " + System.getProperty("MySQL_URL"));
        System.out.println("IDFrom: " + System.getProperty("IDFrom"));
        System.out.println("IDTo: " + System.getProperty("IDTo"));

        // Log each configuration
        log.info("Database Configurations:");
        log.info("DB_Name: {}", DB_Name);
        log.info("UserName_SQL: {}", UserName_SQL);
        log.info("Pass_SQL: {}", Pass_SQL);
        log.info("SQLIP_portal : {}",SQLIP_portal);
        log.info("MYSQL_Name: {}", MYSQL_Name);
        log.info("UserName_MySQL: {}", UserName_MySQL);
        log.info("Pass_MySQL: {}", Pass_MySQL);
        log.info("URL_MySQL: {}", URL_MySQL);
        log.info("MySQL_Vendor: {}",MySQL_Vendor);
        log.info("IDFrom: {}", IDFrom);
        log.info("IDTo: {}", IDTo);
        log.info("browser: {}",browser);
        log.info("mode: {}",mode);
        log.info("url: {}",url);

    }

    @Description("Open Browser and LogIn")
    @BeforeSuite(dependsOnMethods = "SetUp")
    public void Open_Browser() throws InterruptedException {
        try {
            BaseTestDriver= launchBrowser(browser,mode);
            BaseTestDriver.get(url);
        } catch (Exception e) {
            log.error("An error occurred while initializing the browser: {}", browser, e);
        }
    }

    @Description("Recording Starts")
    @BeforeClass
    public void RecordScreen(){
        try {
            String TestClass = this.getClass().getSimpleName();
            // Create a screen recorder and start recording
            recorderUtil = new ScreenRecorderUtil(".\\ScreenRecorder\\"+TestClass+".mp4");
            // Start recording in a separate thread
            new Thread(() -> {
                try {
                    recorderUtil.startRecording();
                    log.info("Recording Starts for : {}",TestClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            log.warn(" The record has problem : ",e);
            e.printStackTrace();
        }
    }

    @Description("Recording Stops")
    @AfterClass
    public void Stop_record() throws Exception {
        recorderUtil.stopRecording();
        System.out.println("Recording stopped.");
        log.info("Recording stopped.");
    }


    @Description("Close the browser")
    @AfterSuite
    public void Close_browser() throws InterruptedException, IOException {
        if (BaseTestDriver!=null){
            BaseTestDriver.findElement(By.xpath("//a[@href='/Admin/Dashboard']")).click();
            BaseTestDriver.findElement(By.xpath("//a[@href='/Account/LogOut']")).click();
            BaseTestDriver.quit();
            log.info("Browser instance closed");
        }
    }

    @Description("Take pic for failed testcases")
    @AfterMethod
    public void failedTestCaseScreen(ITestResult TestCaseResult) throws IOException {
        if (TestCaseResult.getStatus()==ITestResult.FAILURE){
            //Take screenshot and add it to a folder and name it with the testcase name
            File SCREENSHOT =Screenshots.takeshots(BaseTestDriver,".\\screenshots\\"+TestCaseResult.getName()+".png");
            System.out.println("ITestResult.Failure is"+ITestResult.FAILURE);
            System.out.println("TestCaseResult.getStatus()"+TestCaseResult.getStatus());
            Screenshots.takeshots(BaseTestDriver,TestCaseResult.getName());
            //Add the failed test case screenshot in the testing report
            Allure.addAttachment(" Page Screenshot", FileUtils.openInputStream(SCREENSHOT));
        }


    }
    @Description("Compares two PDFs (filePath2 and filePath1) and highlights differences in txt and CSV, extracting text and numerical data.")
    public void ComparePDF(String filePath1, int i, String filePath2) throws IOException {
        log.info("start compare PDFs");
        File file2 = null;
        File file1=new File(filePath1);
        String downloadsPath = System.getProperty("user.home") + "/Downloads";
        File downloadsFolder = new File(downloadsPath);
        // Define output paths for comparison results
        outputPathCompare = ".\\POSFiles\\Difference file\\Compare_Lines_for_id_"+i+"A4.txt";
        outputPathContain = ".\\POSFiles\\Difference file\\Compare_Words_for_id_"+i+"A4.txt";
        outputPathNumbers = ".\\POSFiles\\Difference file\\Compare_Numbers_for_id_"+i+"A4.txt";

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        String webpdf_name= filePath2+"_"+formattedDate;

        // Check if Downloads folder exists and is a directory
        if (downloadsFolder.exists() && downloadsFolder.isDirectory()) {
            // Search for the PDF in Downloads folder
            file2 = searchForPdfInDirectory(downloadsFolder, webpdf_name);
        } else {
            log.info("Downloads folder does not exist.");
        }

        if (file2 != null && FileCompare.isFileDownloaded(file2)) {
            try (PDDocument docFile2 = PDDocument.load(file2);
                 PDDocument docFile1 = PDDocument.load(file1)) {
                if (docFile2 == null) {
                    log.info("Failed to load file1 PDF!");
                }
                if (docFile1 == null) {
                    log.info("Failed to load file2 PDF!");
                }
                // Extract Arabic text from both PDFs
                String textFile2 = FileCompare.extractArabicText(docFile2);
                String textFile1 = FileCompare.extractArabicText(docFile1);
                if (textFile2.equals(textFile1)) {
                    log.info("The contents of both PDFs are identical.");
                } else {
                    log.info("The contents of the two PDFs differ.");
                    // Highlight differences between the two texts
                    FileCompare.highlightDifferences(textFile1, textFile2, outputPathCompare);
                    FileCompare.text_contain(textFile1, textFile2, outputPathContain);
                    // Extract and compare numbers from both PDFs
                    List<String> file1Numbers = FileCompare.extractNumbersFromPDF(file2);
                    List<String> file2Numbers = FileCompare.extractNumbersFromPDF(new File(filePath1));
                    FileCompare.compare_Numbers(file2Numbers, file1Numbers, outputPathNumbers);

                    log.info("file1 Numbers: {}",file1Numbers);
                    log.info("file2 Numbers: {}",file2Numbers);
                    FileCompare.backupPDF(file2, "Path of the file" + i + ".pdf");
                    // Copy the comparison results to CSV files
                    Files.copy(Path.of(outputPathCompare), Path.of("Path of the outputPathCompare" + i + ".csv"), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(Path.of(outputPathContain), Path.of("Path of the outputPathContain" + i + ".csv"), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(Path.of(outputPathNumbers), Path.of("Path of the outputPathNumbers" + i + ".csv"), StandardCopyOption.REPLACE_EXISTING);

                }
            }catch (Exception e){
                log.warn("The Exception Message :",e);
                e.printStackTrace();
            }
        }
    }

    @Description("Deletes a previously downloaded PDF file from the system and logs the result.")
    protected void deleteFile(String filePath) {
        File file ;
        String downloadsPath = System.getProperty("user.home") + "/Downloads";
        File downloadsFolder = new File(downloadsPath);
        if (downloadsFolder.exists() && downloadsFolder.isDirectory()) {
            file = searchForPdfInDirectory(downloadsFolder, filePath);
            if (file == null) {
                log.info("File not found: {}", filePath);
                return;
            }
        } else {
            log.info("Downloads folder does not exist. ");
            return;
        }
        // Attempt to delete the filePath
        if (file.delete()) {
            log.info("PDF filePath deleted successfully: {}", file.getName());
        } else {
            log.info("Failed to delete the PDF filePath: {}", file.getName());
        }
    }

    private File searchForPdfInDirectory(File folder, String fileName) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".pdf") && file.getName().toLowerCase().contains(fileName.toLowerCase())) {
                    return file;  // Return the PDF
                }
            }
        }
        return null;
    }
    private void Clear_Folder(File Folder){
        if (Folder.isDirectory()){
            for(File file : Folder.listFiles()){
                if (file.isDirectory()){
                    Clear_Folder(file);
                }
                file.delete();
            }
        }
    }
    protected void Clear_Downloads(String fileName){
        String downloadsPath = System.getProperty("user.home") + "/Downloads";
        File downloadsDir = new File(downloadsPath);
        if (downloadsDir.exists() && downloadsDir.isDirectory()) {
            File[] files = downloadsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith(fileName) &&
                            (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")||file.getName().endsWith(".pdf"))) {
                        if (file.delete()) {
                            log.info("Deleted: {}", file.getAbsolutePath());
                        } else {
                            log.info("Failed to delete: {}", file.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            log.info("The downloads directory does not exist or has a problem.");
        }
    }

}