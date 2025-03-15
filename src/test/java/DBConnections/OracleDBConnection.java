package DBConnections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Handles Oracle database connections and data retrieval.
 */
public class OracleDBConnection {
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());

    /**
     * Establishes a connection to an Oracle database and executes the given SQL query.
     *
     * @param query   the SQL query to be executed.
     * @param DB_URL  the Oracle database URL (e.g., "jdbc:oracle:thin:@host:port:service").
     * @param DB_name the name of the database.
     * @param user    the username for authentication.
     * @param pass    the password for authentication.
     * @return the ResultSet containing the query results, or null if an error occurs.
     */
    public static ResultSet fetchData(String query, String DB_URL, String DB_name, String user, String pass) {
        ResultSet resultSet = null;
        try {
            String url = DB_URL + "/" + DB_name;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            // Executing SQL query and fetching the result
            resultSet = statement.executeQuery(query);
            log.info("Oracle database connection established successfully.");
            log.info("Oracle Database Name: {}", DB_name);
            log.info("Oracle Username: {}", user);
            log.info("Oracle Password: {}", pass);
            log.info("Oracle Database URL: {}", url);
            log.info("Executed Query: {}", query);

            // Logging database metadata
            DatabaseMetaData dm = connection.getMetaData();
            log.info("Driver Name: {}", dm.getDriverName());
            log.info("Driver Version: {}", dm.getDriverVersion());
            log.info("Database Product Name: {}", dm.getDatabaseProductName());
            log.info("Database Product Version: {}", dm.getDatabaseProductVersion());

        } catch (Exception e) {
            log.error("An error occurred while establishing the Oracle database connection: ", e);
            log.error("Connection failure!");
            log.info("Oracle Database Name: {}", DB_name);
            log.info("Oracle Username: {}", user);
            log.info("Oracle Password: {}", pass);
            log.info("Oracle Database URL: {}", DB_URL);
            log.info("Executed Query: {}", query);
        }
        return resultSet;
    }
}
