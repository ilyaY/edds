package runetsoft.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCTest {
  public static void main(String[] args) throws Exception {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/edds", "root", "ro123ot");
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM CARS");
    while (rs.next()) {
      System.out.println(rs.getInt("ID"));
      System.out.println(rs.getString("NAME"));
    }
    conn.close();
  }
}
