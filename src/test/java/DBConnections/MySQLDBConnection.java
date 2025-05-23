package DBConnections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * Handles MySQL database connections and data retrieval.
 */
public class MySQLDBConnection {
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());

    /**
     * Establishes a connection to a MySQL database and executes the given SQL query.
     *
     * @param query   the SQL query to be executed.
     * @param DB_Name the name of the database.
     * @param URL     the database connection URL.
     * @param user    the username for authentication.
     * @param pass    the password for authentication.
     * @return the ResultSet containing the query results, or null if an error occurs.
     */
    public static ResultSet fetchData(String query, String DB_Name, String URL, String user, String pass) {
        ResultSet resultSet = null;
        try {
            String url = "jdbc:mysql://" + URL + "/" + DB_Name;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            // Executing SQL query and fetching the result
            resultSet = statement.executeQuery(query);
            log.info("The connection has been successfully established.");
            log.info("MYSQL Database Name: {}", DB_Name);
            log.info("MySQL Username: {}", user);
            log.info("MySQL Password: {}", pass);
            log.info("MySQL URL: {}", url);
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
            log.info("MYSQL Database Name: {}", DB_Name);
            log.info("MySQL Username: {}", user);
            log.info("MySQL Password: {}", pass);
            log.info("MySQL URL: {}", URL);
            log.info("Executed Query: {}", query);
        }
        return resultSet;
    }
}
