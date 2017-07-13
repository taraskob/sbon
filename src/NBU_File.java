import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class NBU_File {
    static void CreateFile_03(String day_for, String day_on) {
        Connection Conn = SbonDBConnection.CreateDBConnection();
        if (Conn == null)
            return;
        String dayNumber = getNumber(day_on.substring(0, 2));
        String monthNumber = getNumber(day_on.substring(3, 5));
        String NBUfilename = "#03NIQ" + monthNumber + dayNumber + ".B" + dayNumber + "1";
        Statement statement = null;
        try {
            statement = Conn.createStatement();
            String SQL = "SELECT vkltyp,kodval,sum(Suma) as suma_vkl, sum(Suma*ProcentSt) as proc_vkl from sbon.vklady " +
                    "where date_format(DataOp,\"%d%m%Y\")='" + day_for + "' " +
                    "group by vkltyp,kodval;";
            ResultSet rs = statement.executeQuery(SQL);
            FileWriter writer = new FileWriter(NBUfilename);
            WriteServiceRowsToFile(writer, rs, day_for, day_on, NBUfilename);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("vkltyp") < 10)
                    infRow = "5026";
                infRow = "5031";
                writer.write("1" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
                writer.write("2" + infRow + String.valueOf(rs.getInt("kodval")) + "1=" +
                        AverageProcSt(rs.getInt("suma_vkl"), rs.getFloat("proc_vkl")) + "\r\n");
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

    static String AverageProcSt(int suma_vkl, float proc_vkl) {
        if (suma_vkl == 0)
            return "0.00";
        String pattern = "##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(proc_vkl / suma_vkl).replace(",", ".");
    }


    static void WriteServiceRowsToFile(FileWriter writer, ResultSet rs, String day_for, String day_on, String NBUfilename) {
        try {
            rs.last();
            String count = String.valueOf(rs.getRow());
            rs.beforeFirst();
            writer.write(ServiceLine() + "\r\n");
            Date now = new Date();
            DateFormat formatter = new SimpleDateFormat("HHmm");
            String timeOfFile = formatter.format(now);
            writer.write("02=" + day_for + "=" + day_for + "=" + day_for + "=" + day_on + "=" + timeOfFile + "=325279=01=" + NumberOfRowst(count) +
                    "=" + NBUfilename + "=NNIQ=EDS" + "\r\n");
            writer.write("#1=325279" + "\r\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String NumberOfRowst(String count) {
        return ("000000000" + count).substring(("000000000" + count).length() - 9);
    }

    static String ServiceLine() {
        String space_100 = " ";
        while (space_100.length() < 100)
            space_100 = space_100 + " ";
        return space_100;
    }

    static String getNumber(String str) {

        if (str.charAt(0) == '0')
            return String.valueOf(str.charAt(1));
        return String.valueOf((char) (Integer.parseInt(str) + 55));
    }

}
