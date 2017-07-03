import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

class SbonCreateSchemaDBFromFile {
    static void CreateSchemaDBFromFile(String DBname, String uploadDIR) {
        File fileDB_Stucture = new File(uploadDIR + DBname + ".csv");
        if (!fileDB_Stucture.exists())
            return;
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        Statement statement = null;
        try {
            statement = Conn.createStatement();
            CreateSchema(statement, DBname);
            List<String> lines = Files.readAllLines(Paths.get(String.valueOf(fileDB_Stucture)), Charset.forName("windows-1251"));
            String tableName = null;
            String createScript = null;
            for (String line : lines) {
                if (line.charAt(0) == '{') {
                    tableName = line.substring(1).trim();
                    createScript = "CREATE TABLE IF NOT EXISTS " + DBname + "." + tableName + " (";
                }
                if (line.contains("}")) {
                    createScript = (createScript + line.substring(0, line.lastIndexOf("}"))).replace(",)", ")");

                    CreateTableInsertData(statement, DBname, tableName, createScript, uploadDIR);
                }
                if (!line.contains("}") && !line.contains("{")) {
                    String[] linefields = line.toString().split("\t", 4);
                    for (int i = 0; i < 4; i++) {
                        if (i == 3)
                            createScript = createScript + " COMMENT '" + linefields[i].trim() + "',";
                        else
                            createScript = createScript + linefields[i].trim() + " ";
                    }
                }
            }
            Conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void CreateSchema(Statement stmnt, String dBname) {
        try {
            stmnt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + dBname + " CHARACTER SET cp1251");
            stmnt.executeUpdate("USE " + dBname);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void CreateTableInsertData(Statement stmnt, String DBname, String tableName, String createScript, String uploadDIR) {
        try {
            stmnt.executeUpdate("DROP TABLE IF EXISTS " + DBname + "." + tableName);
            stmnt.executeUpdate(createScript);
            File loadfile = new File(uploadDIR + tableName + ".csv");
            if (loadfile.exists()) {
                String scrpt = "LOAD DATA INFILE '" + uploadDIR + tableName + ".csv' INTO TABLE " + tableName + ";";
                stmnt.executeUpdate(scrpt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
