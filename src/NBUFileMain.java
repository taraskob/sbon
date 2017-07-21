import java.sql.Connection;
import java.sql.SQLException;

class NBUFileMain {
    public static void main(String[] args) {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null) {
            return;
        }
        File_03.CreateFile_03(Conn, "26012009", "27012009");
        File_77.CreateFile_77(Conn, "01012009", "31012009", "01022009");
        MonthlyFiles.CreateFiles(Conn, "01012009", "31012009", "01022009");

    }
}
