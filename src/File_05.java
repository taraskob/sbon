import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class File_05 {
    static void CreateFile_05(String day_begin, String day_end, String day_on) {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        String NBUfilename = "#05NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        Statement statement = null;
        try {
            statement = Conn.createStatement();
            String SQL = "SELECT count(*) as vkl_number,vkltyp,kodval,sum(Suma) as suma_vkl, sum(Suma*ProcentSt) as proc_vkl from sbon.vklady " +
                    "where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' group by vkltyp,kodval;";
            ResultSet rs = statement.executeQuery(SQL);
            FileWriter writer = new FileWriter(NBUfilename);
            NBU_File.WriteServiceRowsToFile(writer, rs, day_begin, day_end, day_on, NBUfilename, 3);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("vkltyp") < 10)
                    infRow = "5311";
                else
                infRow = "5306";
                writer.write("1" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
                writer.write("2" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        NBU_File.AverageProcSt(rs.getInt("suma_vkl"), rs.getFloat("proc_vkl")) + "\r\n");
                writer.write("3" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        String.valueOf(rs.getInt("vkl_number")) + "\r\n");
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
