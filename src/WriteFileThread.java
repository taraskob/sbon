import java.sql.ResultSet;
import java.sql.SQLException;

class WriteFileThread implements Runnable {
    CreateFile FileObj;
    ResultSet rs;
    String day_begin, day_end, day_on;

    WriteFileThread(String day1, String day2, String day3) {
        this.day_begin = day1;
        this.day_end = day2;
        this.day_on = day3;
    }

    Thread t;

    @Override
    public void run() {
        FileObj.CreateFileNN(day_begin, day_end, day_on, rs);
    }

    public void WritToFile(CreateFile filenbu, ResultSet rs) {
        this.rs = rs;
        this.FileObj = filenbu;
        t = new Thread(this);
        t.start();
    }
}
