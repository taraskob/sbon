import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class File_05 implements CreateFile {
    String NBUfilename = null;
    FileCreateListener listener = FileCtrl.getController();

    @Override
    public void CreateFileNN(String day_begin, String day_end, String day_on) {
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        NBUfilename = "#05NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        FileWriter writer = null;

        try {
            Connection Conn = SbonDBConnection.CreateDBConnection();
            if (Conn == null) {
                return;
            }
            ResultSet rs = SQLScripts.getRS_05(Conn, day_begin, day_end);
            writer = new FileWriter(NBUfilename);
            NBU_File.WriteServiceRowsToFile(writer, rs, day_begin, day_end, day_on, NBUfilename, 3);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("vkltyp") < 10) {
                    infRow = "5311";
                } else {
                    infRow = "5306";
                }
                writer.write("1" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
                writer.write("2" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        NBU_File.AverageProcSt(rs.getInt("suma_vkl"), rs.getFloat("proc_vkl")) + "\r\n");
                writer.write("3" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        String.valueOf(rs.getInt("vkl_number")) + "\r\n");
            }
            writer.close();
            listener.fireListener(NBUfilename + " created");
            Conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
