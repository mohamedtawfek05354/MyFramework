package DBConnections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static java.lang.invoke.MethodHandles.lookup;

public class MySQLDBConnection {
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());
    public static ResultSet fetchData(String query,String DB_Name,String URL,String user,String pass) {
        ResultSet resultSet = null;
        try {
            String url = "jdbc:mysql://"+URL+"/"+DB_Name;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            //Executing SQL query and fetching the result
            resultSet = statement.executeQuery(query);
            log.info("The connection has been successfully established.");
            log.info("MYSQL_Name: {}", DB_Name);
            log.info("UserName_MySQL: {}", user);
            log.info("Pass_MySQL: {}", pass);
            log.info("URL_MySQL: {}", url);
            log.info("query:{}",query);
            DatabaseMetaData dm = connection.getMetaData();
            log.info("Driver name: {}", dm.getDriverName());
            log.info("Driver version: {}", dm.getDriverVersion());
            log.info("Product name: {}", dm.getDatabaseProductName());
            log.info("Product version: {}", dm.getDatabaseProductVersion());
        } catch (Exception e) {
            log.error("An error occurred while establishing the connection:");
            e.printStackTrace();
            log.error("Connection failure !!!");
            log.info("MYSQL_Name: {}", DB_Name);
            log.info("UserName_MySQL: {}", user);
            log.info("Pass_MySQL: {}", pass);
            log.info("URL_MySQL: {}", URL);
            log.info("query:{}",query);
        }
        return resultSet;
    }
}