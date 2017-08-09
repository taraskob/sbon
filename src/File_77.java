import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class File_77 implements CreateFile {
    String NBUfilename = null;
    FileCreateListener listener = FileCtrl.getController();
    static void writeInfLine(FileWriter writer, ResultSet rs, String D, String outlay_kod) {
        try {
            while (rs.next()) {
                writer.write("1" + D + String.valueOf(rs.getInt("GroupR031")) + "=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
                writer.write("1" + outlay_kod + String.valueOf(rs.getInt("GroupR031")) + "=" +
                        String.valueOf(rs.getInt("Oz")) + "\r\n");
                writer.write("3" + D + String.valueOf(rs.getInt("GroupR031")) + "=" +
                        String.valueOf(rs.getInt("vkl_number")) + "\r\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void CreateFileNN(String day_begin, String day_end, String day_on) {
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        NBUfilename = "#77NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        Statement statement = null;
        try {
            Connection Conn = SbonDBConnection.CreateDBConnection();
            if (Conn == null) {
                return;
            }
            statement = Conn.createStatement();
            FileWriter writer = new FileWriter(NBUfilename);
            writer.write(NBU_File.ServiceLine() + "\r\n");
            Date now = new Date();
            DateFormat formatter = new SimpleDateFormat("HHmm");
            String timeOfFile = formatter.format(now);
            writer.write("02=" + day_on + "=" + day_begin + "=" + day_end + "=" + day_on + "=" + timeOfFile + "=325279=01=24" +
                    "=" + NBUfilename + "=NNIQ=EDS" + "\r\n");
            writer.write("#1=325279" + "\r\n");
            String SQL = "SELECT round(sum(suma*Kurs_Nbu/Koef)) as suma_vkl,count(*) as vkl_number,GroupR031,sum(Oz7041_1+Oz7041_2+" +
                    "Oz7041_3+Oz7041_4) as Oz from sbon.vklady,sbon.dov_val,sbon.kurs_val " +
                    "where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' and vklady.KodVal=dov_val.CodeR030 and vklady.KodVal=kurs_val.KodVal and ";
            ResultSet rs = statement.executeQuery(SQL + "suma*Kurs_Nbu/Koef<=200000 group by GroupR031;");
            writeInfLine(writer, rs, "1", "9");
            rs = statement.executeQuery(SQL + "suma*Kurs_Nbu/Koef>200000 and suma*Kurs_Nbu/Koef<=350000 group by GroupR031;");
            writeInfLine(writer, rs, "2", "A");
            rs = statement.executeQuery(SQL + "suma*Kurs_Nbu/Koef>350000 and suma*Kurs_Nbu/Koef<=500000 group by GroupR031;");
            writeInfLine(writer, rs, "3", "B");
            rs = statement.executeQuery(SQL + "suma*Kurs_Nbu/Koef>500000 group by GroupR031;");
            writeInfLine(writer, rs, "4", "C");
            writer.close();
            listener.fireListener(NBUfilename + " created");
            rs.close();
            Conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
