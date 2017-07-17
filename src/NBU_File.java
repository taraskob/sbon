import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class NBU_File {
    static String AverageProcSt(int suma_vkl, float proc_vkl) {
        if (suma_vkl == 0)
            return "0.00";
        String pattern = "##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(proc_vkl / suma_vkl).replace(",", ".");
    }

    static void WriteServiceRowsToFile(FileWriter writer, ResultSet rs, String day_Begin, String day_End, String day_on,
                                       String NBUfilename, int koeff) {
        try {
            rs.last();
            String count = String.valueOf(rs.getRow()*koeff);

            rs.beforeFirst();
            writer.write(ServiceLine() + "\r\n");
            Date now = new Date();
            DateFormat formatter = new SimpleDateFormat("HHmm");
            String timeOfFile = formatter.format(now);
            writer.write("02=" + day_on + "=" + day_Begin + "=" + day_End + "=" + day_on + "=" + timeOfFile + "=325279=01=" + NumberOfRowst(count) +
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
        if(str.length()==1)
            return String.valueOf(str);
        return String.valueOf((char) (Integer.parseInt(str) + 55));
    }

    static String date_transform(String day) {
        return day.substring(4)+"-"+day.substring(2,4)+"-"+day.substring(0,2);
    }
}
