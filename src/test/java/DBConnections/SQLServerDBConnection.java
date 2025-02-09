package DBConnections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static java.lang.invoke.MethodHandles.lookup;

public class SQLServerDBConnection {
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());
    public static ResultSet fetchData(String query,String IP_Portal,String DB_name,String user_name,String pass) {
        ResultSet resultSet = null;
        try {
            String url = "jdbc:sqlserver://"+IP_Portal+";databaseName="+DB_name+";encrypt=true;trustServerCertificate=true";
            Connection connection = DriverManager.getConnection(url, user_name, pass);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            log.info("The connection has been successfully established.");
            log.info("MYSQL_Name: {}", DB_name);
            log.info("UserName_MySQL: {}", user_name);
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
            log.info("MYSQL_Name: {}", DB_name);
            log.info("UserName_MySQL: {}", user_name);
            log.info("Pass_MySQL: {}", pass);
            log.info("IP Portal : {}", IP_Portal);
            log.info("query:{}",query);
        }
        return resultSet;
    }

}
