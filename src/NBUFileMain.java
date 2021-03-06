import java.sql.SQLException;

class NBUFileMain {
    public static void main(String[] args) throws SQLException {
        String day_begin, day_end, day_on;
        MultipleInstancesLock miLock = new MultipleInstancesLock("#03");

        if (miLock.isAppActive()) {
            System.out.println("Already active.");
            System.exit(1);
        } else {
            day_begin = "26012009";
            day_on = "27012009";
            File_03 filenbu = new File_03(day_begin, day_on);
            filenbu.CreateFile_03();
        }
        miLock = new MultipleInstancesLock("#monthlyfiles");
        if (miLock.isAppActive()) {
            System.out.println("Already active.");
            System.exit(1);
        } else {
            day_begin = "01012009";
            day_end = "31012009";
            day_on = "01022009";
            MonthlyFiles.CreateFiles(day_begin, day_end, day_on);
        }
    }

}
