package reuse;

import DBConnections.MySQLDBConnection;
import DBConnections.OracleDBConnection;
import DBConnections.SQLServerDBConnection;

import java.sql.ResultSet;

/**
 * Manages database connections and data retrieval for different database types.
 */
public class DBFIleManager {

    private String DB_Type;
    private ResultSet resultSet;

    /**
     * Initializes the database manager with the specified database type.
     *
     * @param DB_Type the type of database ("oracle", "sql", "mysql").
     */
    public DBFIleManager(String DB_Type) {
        this.DB_Type = DB_Type;
    }

    /**
     * Establishes a connection to the database and executes the provided query.
     *
     * @param query   the SQL query to be executed.
     * @param DB_Name the name of the database.
     * @param URL     the database connection URL.
     * @param user    the username for authentication.
     * @param pass    the password for authentication.
     * @return the ResultSet containing the query results, or null if the query fails.
     */
    public ResultSet dbConnection(String query, String DB_Name, String URL, String user, String pass) {
        switch (DB_Type.toLowerCase()) {
            case "sql":
                resultSet = SQLServerDBConnection.fetchData(query, URL, DB_Name, user, pass);
                break;
            case "mysql":
                resultSet = MySQLDBConnection.fetchData(query, DB_Name, URL, user, pass);
                break;
            case "oracle":
                resultSet= OracleDBConnection.fetchData(query,URL,DB_Name,user,pass);
                break;
            default:
                resultSet = null;
                break;
        }
        return resultSet;
    }
}
