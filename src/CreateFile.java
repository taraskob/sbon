import java.sql.ResultSet;
@FunctionalInterface
interface CreateFile {
     void CreateFileNN(String day_begin, String day_end, String day_on, ResultSet rs);
}
