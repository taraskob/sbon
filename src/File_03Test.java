import java.sql.SQLException;

class File_03Test {
    public static void main(String[] args) throws SQLException, InterruptedException {
        String day_begin= "26012009";
        String day_on = "27012009";
        MultipleInstancesLock miLock = new MultipleInstancesLock("#03");
        for (int i = 0; i < 10; i++) {
            if (miLock.isAppActive()) {
                System.out.println("Already active.");
            } else {
                File_03 filenbu = new File_03(day_begin, day_on);
                filenbu.CreateFile_03();
            }
            Thread.sleep(10);
        }
    }

}