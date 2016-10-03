package utils.properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.excel.ExcelReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesReader {

    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    public static String readConfig(String param){
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(".properties");
            prop.load(input);
            return prop.getProperty(param);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return null;
    }
}
