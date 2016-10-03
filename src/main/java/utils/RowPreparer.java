package utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.excel.ExcelReader;

import java.util.ArrayList;

import static utils.properties.PropertiesReader.readConfig;

public class RowPreparer {
    private static ArrayList<ArrayList<String>> rows = new ExcelReader().xlsTableToArrayLists(readConfig("file.name"), readConfig("sheet.name"));
    private ArrayList<String> arrayOfQueries = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(RowPreparer.class);

    private String makeCreationQuery(){
        String creationQuery = "create table " + readConfig("table.name");
        return creationQuery + "( " + prepareColumns(rows.get(0), true) + " )";
    }

    private ArrayList<String> makePrerequisitesQuery(){
        ArrayList<String> prerequisites = new ArrayList<>();
        if (readConfig("db.url").toLowerCase().contains("mysql")) prerequisites.add("SET @@global.max_connections = " + (rows.size() * 2));
        return prerequisites;
    }

    private String makeDroppingQuery(){
        return "drop table if exists " + readConfig("table.name");
    }

    private ArrayList<String> makeInsertionQueries(){
        ArrayList<String> insertionQueries = new ArrayList<>();
        String insertionQuery = "insert into " + readConfig("table.name") + " values ";
        for (int i = 1; i < rows.size(); i++){
            insertionQueries.add(insertionQuery + "( " + prepareColumns(rows.get(i), false) + " )");
        }
        return insertionQueries;
    }

    private String prepareColumns(ArrayList<String> row, boolean isHeader){
        String preparedColumn = "";
        for (String column : row){
            if (isHeader)preparedColumn += headerFormatter(column);
            else preparedColumn += cellFormatter(column);
            if (!row.get(row.size() - 1).equals(column))preparedColumn += ", ";
        }
        return preparedColumn;
    }

    private String headerFormatter(String cell){
            if (cell.length()<1) cell += "_____";
            else if (cell.toLowerCase().equals("group")
                    || cell.toLowerCase().equals("level")
                    || cell.toLowerCase().equals("from")
                    || cell.toLowerCase().equals("create")
                    || cell.toLowerCase().equals("drop")
                    || cell.toLowerCase().equals("select")
                    || cell.toLowerCase().equals("condition")
                    || cell.toLowerCase().equals("value"))
                cell = cell + "_";
            else {
                if (cell.length()>30) cell = cell.substring(0,30);
                cell = cell.replace(" ", "_")
                        .replace("/", "__")
                        .replace("#", "__")
                        .replace("-", "__")
                        .replace("!", "__")
                        .replace("&", "__")
                        .replace("%", "__")
                        .replace("'", "__")
                        .replace("(", "__")
                        .replace(")", "__")
                        .replace("\n", "_")
                ;
            }
        return  cell + " varchar (512)";
    }

    private String cellFormatter(String cell){
        cell = cell.replace("'", "''");
        return "'" + cell + "'";
    }

    public ArrayList<String> prepareArrayOfQueries(){
        arrayOfQueries.addAll(makePrerequisitesQuery());
        arrayOfQueries.add(makeDroppingQuery());
        arrayOfQueries.add(makeCreationQuery());
        arrayOfQueries.addAll(makeInsertionQueries());
        return arrayOfQueries;
    }
}
