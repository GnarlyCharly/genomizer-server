package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.Experiment;

public class SearchDatabaseTests {

    private static String addTestTuplesPath = "/testSuite/add_test_tuples.sql";
    private static String clearTablesPath = "/testSuite/clear_tables.sql";

    public static String username = "genomizer";
    public static String password = "chipseqdata";
    public static String host = "85.226.111.95";
    public static String database = "genomizer";

    private static DatabaseAccessor dbac;
    private static Connection conn;

    private static List<String> addTuplesSqlStrings;
    private static List<String> clearTablesSqlStrings;

    @BeforeClass
    public static void setup() throws Exception {
        dbac = new DatabaseAccessor(username, password, host,
                database);
        
        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        conn = DriverManager.getConnection(url, props);
        
        addTuplesSqlStrings = buildSqlStringsFromFile(addTestTuplesPath);
        
        for (String s: addTuplesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
        
        clearTablesSqlStrings = buildSqlStringsFromFile(clearTablesPath);
    }
    
    private static List<String> buildSqlStringsFromFile(String path) throws UnsupportedEncodingException, IOException {
        List<String> sqlStrings = new ArrayList<String>();
        URL sqlFileUrl = SearchDatabaseTests.class.getResource(path);
        if (sqlFileUrl != null) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(sqlFileUrl.openStream(),
                            "UTF-8"));
            String line = in.readLine();
            StringBuilder statement = new StringBuilder();
            
            while (line != null) {
                statement.append(line);
                if (line.endsWith(";")) {
                    statement.deleteCharAt(statement.length()-1);
                    sqlStrings.add(statement.toString());
                    statement.delete(0, statement.length());
                }
                line = in.readLine();
            }
        } else {
            System.out.println("null url");
        }
        return sqlStrings;
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        for (String s: clearTablesSqlStrings) {
            Statement statement = conn.createStatement();
            statement.execute(s);
        }
        dbac.close();
    }

    @Test
    public void shouldBeAbleToSearchForExperimentUsingPubMedString() throws Exception {
        List<Experiment> experiments = dbac.search("Exp1[ExpID]");
        assertEquals(1, experiments.size());
        assertEquals(2, experiments.get(0).getFiles().size());
    }
    
    @Test
    public void shouldBeAbleToSearchForFilesUsingPubMedString() throws Exception {
        List<Experiment> experiments = dbac.search("/Exp1/Raw/file1.fastq[Path]");
        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());
    }
}