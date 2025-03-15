package DBConnections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Handles SQL Server database connections and data retrieval.
 */
public class SQLServerDBConnection {
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());

    /**
     * Establishes a connection to a SQL Server database and executes the given SQL query.
     *
     * @param query    the SQL query to be executed.
     * @param IP_Portal the IP address or hostname of the SQL Server.
     * @param DB_name  the name of the database.
     * @param user_name the username for authentication.
     * @param pass     the password for authentication.
     * @return the ResultSet containing the query results, or null if an error occurs.
     */
    public static ResultSet fetchData(String query, String IP_Portal, String DB_name, String user_name, String pass) {
        ResultSet resultSet = null;
        try {
            String url = "jdbc:sqlserver://" + IP_Portal + ";databaseName=" + DB_name + ";encrypt=true;trustServerCertificate=true";
            Connection connection = DriverManager.getConnection(url, user_name, pass);
            Statement statement = connection.createStatement();

            // Executing SQL query and fetching the result
            resultSet = statement.executeQuery(query);
            log.info("The connection has been successfully established.");
            log.info("SQL Server Database Name: {}", DB_name);
            log.info("SQL Server Username: {}", user_name);
            log.info("SQL Server Password: {}", pass);
            log.info("SQL Server URL: {}", url);
            log.info("Executed Query: {}", query);

            // Logging database metadata
            DatabaseMetaData dm = connection.getMetaData();
            log.info("Driver Name: {}", dm.getDriverName());
            log.info("Driver Version: {}", dm.getDriverVersion());
            log.info("Database Product Name: {}", dm.getDatabaseProductName());
            log.info("Database Product Version: {}", dm.getDatabaseProductVersion());

        } catch (Exception e) {
            log.error("An error occurred while establishing the connection: ", e);
            log.error("Connection failure!");
            log.info("SQL Server Database Name: {}", DB_name);
            log.info("SQL Server Username: {}", user_name);
            log.info("SQL Server Password: {}", pass);
            log.info("IP Portal: {}", IP_Portal);
            log.info("Executed Query: {}", query);
        }
        return resultSet;
    }
}
