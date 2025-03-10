package reuse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.invoke.MethodHandles.lookup;

public class CSVFileManager {
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());
    private FileReader reader;
    private List<String[]> rows;
    private Map<String,List<String>> ColumnWithRows;
    private CSVParser records;
    private String csvFilePath;
    private FileReader RowReader;
    private Iterable<CSVRecord> RowRecords;

    /**
     * Creates a new instance of the test data CSV reader using the target CSV
     * file path
     *
     * @param csvFilePath target test data CSV file path
     */
    public CSVFileManager(String csvFilePath){
        initializeVariables();
        this.csvFilePath =csvFilePath;
        try {
            reader = new FileReader(csvFilePath);
            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            RowReader = new FileReader(csvFilePath);
            RowRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(RowReader);
            log.info("Reading test data from the following file. [{}]", this.csvFilePath);
        } catch (IOException e) {
            log.error("Couldn't find the desired file. [{}] ", this.csvFilePath, e);
            e.printStackTrace();
        }
    }
    /**
     * Retrieves all rows from the CSV file as a list of string arrays.
     * Each row is represented as an array of strings.
     *
     * @return a list of string arrays, where each array represents a row in the CSV file.
     */
    public List<String[]> getRows(){
        rows = new ArrayList<>();
        try {
            for (CSVRecord record : RowRecords) {
                String[] row = new String[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    row[i] = record.get(i);
                }
                rows.add(row);
            }
            log.info("Successfully retrieved all rows from [{}].", csvFilePath);
        } catch (Exception e) {
            log.error("Error while retrieving rows: {}", e.getMessage());
        }
        return rows;
    }
    /**
     * Retrieves the column names from the CSV file.
     *
     * @return a list of column names as strings.
     */
    public List<String> getColumns(){
        try {
            List<String> columns = new ArrayList<>(records.getHeaderNames());
            log.info("Successfully retrieved column names from [{}].", csvFilePath);
            return columns;
        } catch (Exception e) {
            log.error("Error while retrieving columns: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
    /**
     * Maps each column name to its corresponding list of row data.
     *
     * @return a map where keys are column names and values are lists of column data.
     */
    public Map<String, List<String>> getColumnsWithData() {
        ColumnWithRows = new HashMap<>();
        try {
            List<String[]> rows = getRows();
            List<String> columns = getColumns();
            for (int i = 0; i < columns.size(); i++) {
                List<String> columnData = new ArrayList<>();
                for (String[] row : rows) {
                    if (i < row.length) {
                        columnData.add(row[i]);
                    }
                }
                ColumnWithRows.put(columns.get(i), columnData);
            }
            log.info("You data had been mapped successfully: {}",ColumnWithRows);
        } catch (Exception e) {
            log.error("Error while mapping columns with data: {}", e.getMessage());
        }
        return ColumnWithRows;
    }
    /**
     * Retrieves the name of the last column in the CSV file.
     *
     * @return the name of the last column, or null if an error occurs.
     */
    public String getLastColumn(){
        try {
            List<String> columns = getColumns();
            log.info("The last Columns had been retrieved successfully : {}",columns.getLast());
            return columns.getLast();
        } catch (Exception e) {
            log.error("Error while retrieving the last column: {}", e.getMessage());
            return null;
        }
    }
    /**
     * Retrieves the name of a specific column based on its index.
     *
     * @param ColumnNum the 1-based index of the column.
     * @return the column name at the specified index, or null if an error occurs.
     */
    public String getSpecificColumnName(int ColumnNum){
        try {
            log.info("Get Specific Column Name had been worked successfully: {} at index {}", getColumns().get(ColumnNum - 1),ColumnNum);
            return getColumns().get(ColumnNum - 1);
        } catch (Exception e) {
            log.error("Error while retrieving column name at index {}: {}", ColumnNum, e.getMessage());
            return null;
        }
    }
    /**
     * Retrieves all data for a specific column.
     *
     * @param ColumnName the name of the column.
     * @return a list of strings containing the column data, or an empty list if an error occurs.
     */
    public List<String> GetSpecificColumnData(String ColumnName){
        try {
            log.info("Get Specific Column Data had been worked successfully:{} and column: {}", getColumnsWithData().get(ColumnName),ColumnName);
            return getColumnsWithData().get(ColumnName);
        } catch (Exception e) {
            log.error("Error while retrieving data for column: {}. {}", ColumnName, e.getMessage());
            return Collections.emptyList();
        }
    }
    /**
     * Retrieves all data for a specific column.
     *
     * @param ColumnIndex the 1-based index of the column.
     * @return a list of strings containing the column data, or an empty list if an error occurs.
     */
    public List<String> GetSpecificColumnData(int ColumnIndex){
        try {
            log.info("Get Specific Column Data had been worked successfully:{} and columnIndex: {}",getColumnsWithData().get(getSpecificColumnName(ColumnIndex-1)),ColumnIndex);
            return getColumnsWithData().get(getSpecificColumnName(ColumnIndex-1));
        } catch (Exception e) {
            log.error("Error while retrieving data for columnIndex: {}. {}", ColumnIndex, e.getMessage());
            return Collections.emptyList();
        }
    }
    /**
     * Retrieves a specific cell's data based on row number and column name.
     *
     * @param RowNum the 0-based index of the row.
     * @param ColumnName the name of the column.
     * @return the data in the specified cell, or null if an error occurs.
     */
    public String getCellData(int RowNum, String ColumnName) {
        try {
            String[] row = getRows().get(RowNum);
            List<String> columns = getColumns();
            return row[columns.indexOf(ColumnName)];
        } catch (Exception e) {
            log.error("Error while retrieving cell data for Row: {}, Column: {}. {}", RowNum, ColumnName, e.getMessage());
            return null;
        }
    }
    /**
     * Retrieves a specific cell's data based on row number and column name.
     *
     * @param RowNum the 0-based index of the row.
     * @param ColumnIndex the 1-based index of the column.
     * @return the data in the specified cell, or null if an error occurs.
     */
    public String getCellData(int RowNum, int ColumnIndex) {
        try {
            String[] row = getRows().get(RowNum);
            List<String> columns = getColumns();
            return row[columns.indexOf(getSpecificColumnName(ColumnIndex-1))];
        } catch (Exception e) {
            log.error("Error while retrieving cell data for Row: {}, ColumnIndex: {}. {}", RowNum, ColumnIndex, e.getMessage());
            return null;
        }
    }
    /**
     * Retrieves the name of the first column in the CSV file.
     *
     * @return the name of the first column,or null if an error occurs.
     */
    public String getFirstColumn(){
        try {
            List<String> columns = getColumns();
            log.info("Successfully retrieved the first column name from [{}].", csvFilePath);
            return columns.getFirst();
        } catch (Exception e) {
            log.error("Error while retrieving the first column: {}", e.getMessage());
            return null;
        }
    }
    /**
     * Retrieves the minimum value from a specific column.
     *
     * @param columnName the name of the column.
     * @return the minimum value in the column, or Double.NaN if an error occurs.
     */
    public double getMinCellValue(String columnName) {
        try {
            List<String> columnData = getColumnsWithData().get(columnName);
            double min = Double.MAX_VALUE;
            for (String value : columnData) {
                double num = Double.parseDouble(value);
                if (num < min) {
                    min = num;
                }
            }
            log.info("Successfully retrieved Min cell value= {} of column : {} from [{}].", min, columnName, csvFilePath);
            return min;
        } catch (Exception e) {
            log.error("Error calculating min value for column: {}. {}", columnName, e.getMessage());
            return Double.NaN;
        }
    }
    /**
     * Retrieves the minimum value from a specific column.
     *
     * @param columnIndex the index of the column.
     * @return the minimum value in the column, or Double.NaN if an error occurs.
     */
    public double getMinCellValue(int columnIndex) {
        try {
            List<String> columnData = getColumnsWithData().get(getColumns().get(columnIndex));
            double min = Double.MAX_VALUE;
            for (String value : columnData) {
                double num = Double.parseDouble(value);
                if (num < min) {
                    min = num;
                }
            }
            log.info("Successfully retrieved Min cell value= {} of column: {} from [{}].", min, getColumns().get(columnIndex), csvFilePath);
            return min;
        } catch (Exception e) {
            log.error("Error calculating min value for column: " + getColumns().get(columnIndex) + ". " + e.getMessage());
            return Double.NaN;
        }
    }
    /**
     * Retrieves the maximum value from a specific column.
     *
     * @param columnName the name of the column.
     * @return the maximum value in the column, or Double.NaN if an error occurs.
     */
    public double getMaxCellValue(String columnName) {
        try {
            List<String> columnData = getColumnsWithData().get(columnName);
            double max = Double.MIN_VALUE;
            for (String value : columnData) {
                double num = Double.parseDouble(value);
                if (num > max) {
                    max = num;
                }
            }
            log.info("Successfully retrieved Max cell value= {} of column : {} from [{}].", max, columnName, csvFilePath);
            return max;
        } catch (Exception e) {
            log.error("Error calculating min value for column: {}. {}", columnName, e.getMessage());
            return Double.NaN;
        }
    }
    /**
     * Retrieves the maximum value from a specific column.
     *
     * @param columnIndex the name of the column.
     * @return the maximum value in the column, or Double.NaN if an error occurs.
     */
    public double getMaxCellValue(int columnIndex) {
        try {
            List<String> columnData = getColumnsWithData().get(getColumns().get(columnIndex));
            double max = Double.MIN_VALUE;
            for (String value : columnData) {
                double num = Double.parseDouble(value);
                if (num > max) {
                    max = num;
                }
            }
            log.info("Successfully retrieved Max cell value = {} of column : {} from [{}].", max, getColumns().get(columnIndex), csvFilePath);
            return max;
        } catch (Exception e) {
            log.error("Error calculating min value for column: " + getColumns().get(columnIndex) + ". " + e.getMessage());
            return Double.NaN;
        }
    }
    /**
     * Retrieves the total count of cells in a specific column.
     *
     * @param columnName the name of the column.
     * @return the number of cells in the column, or 0 if an error occurs.
     */
    public int getCellCount(String columnName) {
        try {
            log.info("Successfully retrieved cell count of column : {} from [{}].", columnName, csvFilePath);
            return getColumnsWithData().get(columnName).size();
        } catch (Exception e) {
            log.error("Error calculating count for column: {}. {}", columnName, e.getMessage());
            return 0;
        }
    }
    /**
     * Retrieves the total count of cells in a specific column.
     *
     * @param columnIndex the name of the column.
     * @return the number of cells in the column, or 0 if an error occurs.
     */
    public int getCellCount(int columnIndex) {
        try {
            log.info("Successfully retrieved cell count of column : "+getColumns().get(columnIndex)+" from ["+csvFilePath+"].");
            return getColumnsWithData().get(getColumns().get(columnIndex)).size();
        } catch (Exception e) {
            log.error("Error calculating count for column: {}. {}", getColumns().get(columnIndex), e.getMessage());
            return 0;
        }
    }
    private void initializeVariables() {
        reader = null;
        RowReader = null;
        records =null;
        RowRecords=null;
        rows = null;
        ColumnWithRows=null;
        csvFilePath = "";
    }
}