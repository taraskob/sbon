import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class File_91 {
    static void CreateFile_91(String day_begin, String day_end, String day_on) {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        String NBUfilename = "#91NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        Statement statement = null;
        try {
            statement = Conn.createStatement();
            FileWriter writer = new FileWriter(NBUfilename);
            writer.write(NBU_File.ServiceLine() + "\r\n");
            Date now = new Date();
            DateFormat formatter = new SimpleDateFormat("HHmm");
            String timeOfFile = formatter.format(now);
            String SQL = "SELECT sum(round(suma*Kurs_Nbu/Koef)) as suma_vkl,vklady.kodval,kodstrok,idn_podatk,client.name,country,resident" +
                    " from sbon.vklady,sbon.client,sbon.kurs_val," +
                    "sbon.dov_vkl where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' and vklady.KodVal=kurs_val.KodVal and vklady.asnumber=client.asnumber and " +
                    "suma>0 group by vklady.asnumber,suma desc;";
            ResultSet rs = statement.executeQuery(SQL);
            rs.last();
            String count = "120";
            if (rs.getRow() < 20)
                count = String.valueOf(rs.getRow() * 6);
            rs.beforeFirst();
            writer.write("02=" + day_on + "=" + day_begin + "=" + day_end + "=" + day_on + "=" + timeOfFile + "=325279=01="+count+
                    "=" + NBUfilename + "=NNIQ=EDS" + "\r\n");
            writer.write("#1=325279" + "\r\n");
            int seq_number=0;

            while (rs.next()) {
                seq_number+=1;
               writer.write("05"+(NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) +"00000000=" +
                       rs.getString("idn_podatk")) + "\r\n");
                writer.write("06"+(NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) +"00000000=" +
                        String.valueOf(rs.getInt("country")) + "\r\n"));
                writer.write("07"+(NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) +"00000000=0" +"\r\n"));
                writer.write("08"+(NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) +"00000000=" +
                        rs.getString("name")) + "\r\n");
                writer.write("09"+(NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) +"00000000=" +
                        String.valueOf(rs.getInt("resident")) + "\r\n"));
               writer.write("20"+(NBU_File.NumberOfRows(String.valueOf(seq_number)).substring(7) +"00000000=" +
                        String.valueOf(rs.getBigDecimal("suma_vkl")) + "\r\n"));
                if(seq_number==20)
                    break;
            }
            writer.close();
            rs.close();
            statement.close();
            Conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
