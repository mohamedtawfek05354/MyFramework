package reuse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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

    public List<String> getHeaders(){
        try {
            List<String> columns = new ArrayList<>(records.getHeaderNames());
            log.info("Successfully retrieved column names from [{}].", csvFilePath);
            return columns;
        } catch (Exception e) {
            log.error("Error while retrieving columns: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
    public Map<String, List<String>> getColumnsWithData() {
        ColumnWithRows = new HashMap<>();
        try {
            List<String[]> rows = getRows();
            List<String> columns = getHeaders();
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
    public String getLastHeader(){
        try {
            List<String> columns = getHeaders();
            log.info("The last Columns had been retrieved successfully : {}",columns.getLast());
            return columns.getLast();
        } catch (Exception e) {
            log.error("Error while retrieving the last column: {}", e.getMessage());
            return null;
        }
    }
    public String getSpecificHeaderName(int ColumnNum){
        try {
            log.info("Get Specific Column Name had been worked successfully: {} at index {}", getHeaders().get(ColumnNum - 1),ColumnNum);
            return getHeaders().get(ColumnNum - 1);
        } catch (Exception e) {
            log.error("Error while retrieving column name at index {}: {}", ColumnNum, e.getMessage());
            return null;
        }
    }
    public List<String> GetSpecificHeaderData(String ColumnName){
        try {
            log.info("Get Specific Column Data had been worked successfully:{} and column: {}", getColumnsWithData().get(ColumnName),ColumnName);
            return getColumnsWithData().get(ColumnName);
        } catch (Exception e) {
            log.error("Error while retrieving data for column: {}. {}", ColumnName, e.getMessage());
            return Collections.emptyList();
        }
    }
    public String getCellData(int RowNum, String ColumnName) {
        try {
            String[] row = getRows().get(RowNum);
            List<String> columns = getHeaders();
            log.info("get Cell Data had been worked successfully: {} and Row: {}, Column: {}",row[columns.indexOf(ColumnName)], RowNum, ColumnName);
            return row[columns.indexOf(ColumnName)];
        } catch (Exception e) {
            log.error("Error while retrieving cell data for Row: {}, Column: {}. {}", RowNum, ColumnName, e.getMessage());
            return null;
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