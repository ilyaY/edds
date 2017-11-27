package runetsoft.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCTest {
  public static void main(String[] args) throws Exception {
    if (args == null || args.length != 3) {
      System.out.println("Usage : JDBCTest [db_url] [db_username] [db_password]");
      System.exit(0);
    }

    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    Connection conn = DriverManager.getConnection(args[0], args[1], args[2]);
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM CARS");
    while (rs.next()) {
      System.out.println(rs.getInt("ID"));
      System.out.println(rs.getString("NAME"));
      System.out.println(rs.getString("CAR_KEY"));
    }
    conn.close();
  }
}
