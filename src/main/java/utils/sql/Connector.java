package utils.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import static utils.properties.PropertiesReader.readConfig;

public class Connector {

    public static Connection getConnection() throws Exception {
        String url = readConfig("db.url");
        assert url != null;
        if (url.contains("oracle"))Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(url, readConfig("db.login"), readConfig("db.password"));
    }
}
