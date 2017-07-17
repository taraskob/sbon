import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class File_03 {
    static void CreateFile_03(String day_for, String day_on) {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        String NBUfilename = "#03NIQ" + monthNumber + dayNumber + ".B" + dayNumber + "1";
        Statement statement = null;
        try {
            statement = Conn.createStatement();
            String SQL = "SELECT vkltyp,kodval,sum(Suma) as suma_vkl, sum(Suma*ProcentSt) as proc_vkl from sbon.vklady " +
                    "where date_format(DataOp,\"%d%m%Y\")='" + day_for + "' " +
                    "group by vkltyp,kodval;";
            ResultSet rs = statement.executeQuery(SQL);
            FileWriter writer = new FileWriter(NBUfilename);
            NBU_File.WriteServiceRowsToFile(writer, rs, day_for,day_for, day_on, NBUfilename,2);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("vkltyp") < 10)
                    infRow = "5031";
                else
                infRow = "5026";
                writer.write("1" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
                writer.write("2" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        NBU_File.AverageProcSt(rs.getInt("suma_vkl"), rs.getFloat("proc_vkl")) + "\r\n");
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
