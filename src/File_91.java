import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class File_91 implements CreateFile {
    String NBUfilename = null;
    FileCreateListener listener = FileCtrl.getController();
    @Override
    public void CreateFileNN(String day_begin, String day_end, String day_on) {
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        NBUfilename = "#91NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        FileWriter writer = null;
        try {
            Connection Conn = SbonDBConnection.CreateDBConnection();
            if (Conn == null) {
                return;
            }
            ResultSet rs = SQLScripts.getRS_91(Conn, day_begin, day_end);
            writer = new FileWriter(NBUfilename);
            writer.write(NBU_File.ServiceLine() + "\r\n");
            Date now = new Date();
            DateFormat formatter = new SimpleDateFormat("HHmm");
            String timeOfFile = formatter.format(now);
            rs.last();
            String count = "120";
            if (rs.getRow() < 20) {
                count = String.valueOf(rs.getRow() * 6);
            }
            rs.beforeFirst();
            writer.write("02=" + day_on + "=" + day_begin + "=" + day_end + "=" + day_on + "=" + timeOfFile + "=325279=01=" + count +
                    "=" + NBUfilename + "=NNIQ=EDS" + "\r\n");
            writer.write("#1=325279" + "\r\n");
            int seq_number = 0;
            while (rs.next()) {
                seq_number += 1;
                writer.write("05" + (NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) + "00000000=" +
                        rs.getString("idn_podatk")) + "\r\n");
                writer.write("06" + (NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) + "00000000=" +
                        String.valueOf(rs.getInt("country")) + "\r\n"));
                writer.write("07" + (NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) + "00000000=0" + "\r\n"));
                writer.write("08" + (NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) + "00000000=" +
                        rs.getString("name")) + "\r\n");
                writer.write("09" + (NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) + "00000000=" +
                        String.valueOf(rs.getInt("resident")) + "\r\n"));
                writer.write("20" + (NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) + "00000000=" +
                        String.valueOf(rs.getBigDecimal("suma_vkl")) + "\r\n"));
                if (seq_number == 20) {
                    break;
                }
            }
            writer.close();
            listener.fireListener(NBUfilename+" created");
            rs.close();
            Conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
