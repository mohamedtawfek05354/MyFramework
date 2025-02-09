package reuse;

import DBConnections.MySQLDBConnection;
import DBConnections.SQLServerDBConnection;

import java.sql.ResultSet;

public class DBFIleManager {

    private String DB_Type;
    private ResultSet resultSet;
    public DBFIleManager(String DB_Type){
        this.DB_Type=DB_Type;
    }
    public ResultSet dbConnection(String query,String DB_Name,String URL,String user,String pass){
        switch (DB_Type.toLowerCase()){
            case "sql":
                resultSet= SQLServerDBConnection.fetchData( query, URL, DB_Name, user, pass);
                break;
            case "mysql":
                resultSet= MySQLDBConnection.fetchData( query, DB_Name,URL, user, pass);
                break;
        }
        return resultSet;
    }
}
