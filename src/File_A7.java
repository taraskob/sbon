import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

class File_A7 implements CreateFile {
    @Override
    public void CreateFileNN(String day_begin, String day_end, String day_on, ResultSet rs) {
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        String NBUfilename = "#A7NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        try {
            FileWriter writer = new FileWriter(NBUfilename);
            NBU_File.WriteServiceRowsToFile(writer, rs, day_begin, day_end, day_on, NBUfilename, 1);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("kodstrok") < 12) {
                    infRow = "22181";
                } else {
                    infRow = "22192";
                }

                writer.write(infRow + NBU_File.getNumber(String.valueOf(rs.getInt("kodstrok"))) +
                        String.valueOf(rs.getInt("GroupR031")) + "=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
            }
            writer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
