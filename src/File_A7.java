import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class File_A7 {
    static void CreateFile_A7(String day_begin, String day_end, String day_on) {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        String NBUfilename = "#A7NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        Statement statement = null;
        try {
            statement = Conn.createStatement();
            String SQL = "SELECT round(sum(suma*Kurs_Nbu/Koef)) as suma_vkl,GroupR031,kodstrok from sbon.vklady,sbon.dov_val,sbon.kurs_val, " +
                    " sbon.dov_vkl where DataOp between '" + NBU_File.date_transform(day_begin) + "' and '"
                    + NBU_File.date_transform(day_end) + "' and sbon.vklady.KodVal=sbon.dov_val.CodeR030 and sbon.vklady.KodVal=sbon.kurs_val.KodVal and " +
                    "sbon.vklady.ShifrVkl=sbon.dov_vkl.Shifr and sbon.vklady.vkltypaggr>1 group by GroupR031,KodStrok;";
            ResultSet rs = statement.executeQuery(SQL);
            FileWriter writer = new FileWriter(NBUfilename);
            NBU_File.WriteServiceRowsToFile(writer, rs, day_begin, day_end, day_on, NBUfilename, 1);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("kodstrok") < 12)
                    infRow = "22181";
                else
                    infRow = "22192";

                writer.write(infRow + NBU_File.getNumber(String.valueOf(rs.getInt("kodstrok"))) +
                        String.valueOf(rs.getInt("GroupR031")) + "=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
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
