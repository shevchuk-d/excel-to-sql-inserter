import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RowPreparer;

import java.util.ArrayList;

import static utils.sql.QueryExecutor.executeQueryForConnection;


public class MainExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MainExecutor.class);

    public static void main(String[] args) throws Exception {
        logger.info("Insertion has been started");
        executeQueryForConnection("SET @@global.max_connections = 14400");
        RowPreparer rowPreparer = new RowPreparer();
        ArrayList<String> queries = rowPreparer.prepareArrayOfQueries();
        int rowNumber = 1;
        for (String query: queries){
            logger.info((query.contains("insert into") ? "Insert row # " + rowNumber++ + ": " : "") + query);
            executeQueryForConnection(query);
        }
        logger.info("Insertion has been finished");
    }
}
