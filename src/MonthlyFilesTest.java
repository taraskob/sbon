import java.sql.SQLException;

public class MonthlyFilesTest {
    public static void main(String[] args) throws SQLException, InterruptedException {
        String day_begin = "01012009";
        String day_end = "31012009";
        String day_on = "01022009";
        MultipleInstancesLock miLock = new MultipleInstancesLock("#monthlyfiles");
        for (int i = 0; i < 10; i++) {
            if (miLock.isAppActive()) {
                System.out.println("Already active.");
            } else {
                MonthlyFiles.CreateFiles(day_begin, day_end, day_on);
            }
            Thread.sleep(10);
        }
    }
}
