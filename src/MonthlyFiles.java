import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class MonthlyFiles {
    static void CreateFiles(Connection conn, String day_begin, String day_end, String day_on) {
        WriteFileThread wft = new WriteFileThread("01012009", "31012009", "01022009");
        try {
            ResultSet rs = SQLScripts.getRS_05(conn, "01012009", "31012009");
            CreateFile filenbu = new File_05();
            wft.WritToFile(filenbu, rs);
            rs = SQLScripts.getRS_06(conn, "01012009", "31012009");
            filenbu = new File_06();
            wft.WritToFile(filenbu, rs);
            rs = SQLScripts.getRS_91(conn, "01012009", "31012009");
            filenbu = new File_91();
            wft.WritToFile(filenbu, rs);
            rs = SQLScripts.getRS_A7(conn, "01012009", "31012009");
            filenbu = new File_A7();
            wft.WritToFile(filenbu, rs);
            wft.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
