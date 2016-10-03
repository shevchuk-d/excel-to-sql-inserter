package utils.excel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RowPreparer;

import java.io.FileInputStream;
import java.util.ArrayList;


public class ExcelReader {

    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    public  ArrayList<ArrayList<String>> xlsTableToArrayLists(String fileName, String sheetName){
        int maxCellNum;
        XSSFSheet orders = null;
        XSSFWorkbook book;
        try {
            book = new XSSFWorkbook(new FileInputStream(fileName));
            orders = book.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert orders != null : "Sheet " + sheetName + " is NULL";
        maxCellNum = orders.getRow(0).getLastCellNum();

        logger.info("Amount of rows for insert: " + (orders.getLastRowNum()));

        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        for (int rowNumber = 0; rowNumber <= orders.getLastRowNum(); rowNumber++) {
            ArrayList<String> cells  = new ArrayList<>();
            for (int cellNumber = 0; cellNumber < maxCellNum; cellNumber++) {
                String stringCellValue = "";
                try {
                    orders.getRow(rowNumber).getCell(cellNumber);
                    stringCellValue = orders.getRow(rowNumber).getCell(cellNumber).getStringCellValue();
                }catch (NullPointerException e){
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
                catch (IllegalStateException ise){stringCellValue = orders.getRow(rowNumber).getCell(cellNumber).getNumericCellValue()+"";}

                cells.add(stringCellValue.replace("'", "''"));
            }
            rows.add(cells);
        }
        return rows;
    }

}
