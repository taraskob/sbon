import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class File_03 implements Runnable, CreateFile {
    Thread t;
    String day_for;
    String day_on;
    String NBUfilename = null;

    File_03(String day_for, String day_on) {
        this.day_on = day_on;
        this.day_for = day_for;
        t = new Thread(this);
    }

    FileCreateListener listener = FileCtrl.getController();

    public void CreateFile_03() {
        t.start();
    }

    @Override
    public void run() {
        this.CreateFileNN(day_for, day_for, day_on);
    }

    @Override
    public void CreateFileNN(String day_begin, String day_end, String day_on) {
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        NBUfilename = "#03NIQ" + monthNumber + dayNumber + ".B" + dayNumber + "1";
        try {
            Connection Conn = SbonDBConnection.CreateDBConnection();
            if (Conn == null) {
                return;
            }
            ResultSet rs = SQLScripts.getRS_03(Conn, day_for);
            FileWriter writer = new FileWriter(NBUfilename);
            NBU_File.WriteServiceRowsToFile(writer, rs, day_for, day_for, day_on, NBUfilename, 2);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("vkltyp") < 10) {
                    infRow = "5031";
                } else {
                    infRow = "5026";
                }
                writer.write("1" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
                writer.write("2" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        NBU_File.AverageProcSt(rs.getInt("suma_vkl"), rs.getFloat("proc_vkl")) + "\r\n");
            }
            listener.fireListener(NBUfilename + " created");
            writer.close();
            rs.close();
            Conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
