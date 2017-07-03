import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class SbonDBConnection {
    static Connection CreateDBConnection() {
        Connection Conn = null;
        try {
            Conn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/?useSSL=false", "root", "12345");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Conn;
    }
}
