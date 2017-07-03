import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class SbonInsertData {
    static void InsertData() {
        try {
            InsertData("account");
            InsertData("client");
            InsertData("acntbaln");
            InsertData("dov_anal");
            InsertData("dov_convoper");
            InsertData("dov_isek");
            InsertData("dov_symz");
            InsertData("dov_val");
            InsertData("dov_vkl");
            InsertData("dovstrok");
            InsertData("kurs_val");
            InsertData("vklady");
            InsertData("operjrnl");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void InsertData(String filename) throws SQLException {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        Statement s = Conn.createStatement();
        int Result = s.executeUpdate("USE sbon");
        //  s.executeUpdate("LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/account.csv' INTO TABLE account;");
        String scrpt = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/" + filename + ".csv' INTO TABLE " + filename + ";";
        s.executeUpdate(scrpt);
        Conn.close();
    }
}
