package reuse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelFileManager is a utility class to handle Excel file operations.
 * It provides methods to read data from an Excel sheet including headers, row data, and column data.
 */
public class ExcelFileManager {

    private static final Logger log = LogManager.getLogger(ExcelFileManager.class);
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    /**
     * Constructor to initialize and load an Excel file and a specific sheet.
     *
     * @param excelFilePath the path to the Excel file.
     * @param sheetName the name of the sheet to be loaded.
     */
    public ExcelFileManager(String excelFilePath, String sheetName) {
        initValues();
        try {
            log.info("Opening Excel file: {}", excelFilePath);
            FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath));
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheet(sheetName);
            log.info("Sheet loaded: {}", sheetName);
        } catch (IOException e) {
            log.error("Error loading Excel file: {}", e.getMessage(), e);
        }
    }
    /**
     * Retrieves the total number of rows in the Excel sheet.
     *
     * @return the number of physical rows in the sheet.
     */
    public int getRowCount() {
        int rowCount = sheet.getPhysicalNumberOfRows();
        log.info("Total rows: {}", rowCount);
        return rowCount;
    }

    /**
     * Retrieves the total number of columns in the first row of the sheet.
     *
     * @return the number of columns in the sheet.
     */
    public int getColumnCount() {
        int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
        log.info("Total columns: {}", columnCount);
        return columnCount;
    }
    /**
     * Retrieves the formula present in a specific cell if available.
     *
     * @param rowNum the 0-based index of the row.
     * @param colNum the 0-based index of the column.
     * @return the formula as a string, or a message if no formula is found.
     */
    public String getSpecificCellFormula(int rowNum, int colNum) {
        Cell cell = sheet.getRow(rowNum).getCell(colNum);
        if (cell != null && cell.getCellType() == CellType.FORMULA) {
            log.info("Formula found at row: {}, column: {}", rowNum, colNum);
            return cell.getCellFormula();
        } else {
            log.warn("No formula found at row: {}, column: {}", rowNum, colNum);
            return "There's No Formula here";
        }
    }
    /**
     * Retrieves the data present in a specific cell.
     *
     * @param rowNum the 0-based index of the row.
     * @param colNum the 0-based index of the column.
     * @return the cell data as a formatted string.
     */
    public String getSpecificCellData(int rowNum, int colNum) {
        Cell cell = sheet.getRow(rowNum).getCell(colNum);
        DataFormatter formatter = new DataFormatter();
        String cellData = formatter.formatCellValue(cell);
        log.info("Cell data at row {}, column {}: {}", rowNum, colNum, cellData);
        return cellData;
    }
    /**
     * Retrieves all column headers from the first row of the sheet.
     *
     * @return a list of header names.
     */
    public List<String> getHeaders() {
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < getColumnCount(); i++) {
            headers.add(getSpecificCellData(0, i));
        }
        log.info("Headers retrieved: {}", headers);
        return headers;
    }
    /**
     * Retrieves all column data mapped to their respective headers.
     *
     * @return a map where keys are column headers and values are lists of column data.
     */
    public Map<String, List<String>> getColumnsData() {
        Map<String, List<String>> columnData = new LinkedHashMap<>();
        try {
            List<String[]> rows = getRows();
            List<String> headers = getHeaders();
            for (int i = 0; i < headers.size(); i++) {
                List<String> columnValues = new ArrayList<>();
                for (String[] row : rows) {
                    if (i < row.length) {
                        columnValues.add(row[i]);
                    }
                }
                columnData.put(headers.get(i), columnValues);
            }
            log.info("Column data retrieved successfully");
        } catch (Exception e) {
            log.error("Error retrieving column data: {}", e.getMessage(), e);
        }
        return columnData;
    }
    /**
     * Retrieves all rows of data from the sheet, excluding headers.
     *
     * @return a list of string arrays where each array represents a row.
     */
    public List<String[]> getRows() {
        List<String[]> data = new ArrayList<>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            String[] rowdata = new String[getColumnCount()];
            for (int j = 0; j < getColumnCount(); j++) {
                rowdata[j] = getSpecificCellData(i, j);
            }
            data.add(rowdata);
        }
        log.info("Rows retrieved: {}", data.size());
        return data;
    }
    /**
     * Closes the Excel file and releases resources.
     */
    public void closeExcelFile() {
        try {
            if (workbook != null) {
                workbook.close();
                log.info("Excel file closed successfully.");
            }
        } catch (IOException e) {
            log.error("Error closing Excel file: {}", e.getMessage(), e);
        }
    }
    /**
     * Initializes the workbook and sheet variables.
     */
    private void initValues() {
        workbook = null;
        sheet = null;
        log.info("ExcelFileManager initialized.");
    }
}
