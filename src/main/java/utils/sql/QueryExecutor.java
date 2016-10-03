package utils.sql;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

import static utils.sql.Connector.getConnection;


public class QueryExecutor {

    private static final Logger logger = LoggerFactory.getLogger(QueryExecutor.class);

    public static void executeQueryForConnection (String query) throws Exception {
        Connection connection = getConnection();
        executeQueryForConnectionWithExceptionCatching(query);
        connection.close();
    }

    public static void executeQueryForConnectionWithExceptionCatching(String query){
        try {
            getConnection().createStatement().executeUpdate(query);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

}