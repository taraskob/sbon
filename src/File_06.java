import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

class File_06 implements CreateFile {

    @Override
    public void CreateFileNN(String day_begin, String day_end, String day_on, ResultSet rs) {
        String dayNumber = NBU_File.getNumber(day_on.substring(0, 2));
        String monthNumber = NBU_File.getNumber(day_on.substring(2, 4));
        String NBUfilename = "#06NIQ" + monthNumber + dayNumber + ".B" + monthNumber + "1";
        FileWriter writer = null;
        try {
            writer = new FileWriter(NBUfilename);
            NBU_File.WriteServiceRowsToFile(writer, rs, day_begin, day_end, day_on, NBUfilename, 1);
            String infRow = null;
            while (rs.next()) {
                if (rs.getInt("vkltypaggr") < 2) {
                    infRow = "2262009931";
                } else {
                    infRow = "2263009931";
                }
                writer.write(infRow + String.valueOf(rs.getInt("GroupR031")) + "110=" +
                        String.valueOf(rs.getInt("suma_vkl")) + "\r\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
