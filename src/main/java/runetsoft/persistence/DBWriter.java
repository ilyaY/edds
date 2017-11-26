package runetsoft.persistence;

import runetsoft.DataEntry;
import runetsoft.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class DBWriter {
  private final String myUrl;
  private final String myUsername;
  private final String myPassword;
  private final ExecutorService myService = Executors.newSingleThreadExecutor();

  public DBWriter(String url, String username, String password) {
    myUrl = url;
    myUsername = username;
    myPassword = password;
  }

  public void write(String carKey, DataEntry entry) {
    myService.submit(() -> {
      try (Connection conn = DriverManager.getConnection(myUrl, myUsername, myPassword)) {
        int carId = getCarId(conn, carKey);
        executeUpdate(conn, carId, entry);
      } catch (Exception e) {
        Main.LOG.log(Level.SEVERE, "DB.write()", e);
        throw new RuntimeException(e);
      }
    });
  }

  private int getCarId(Connection conn, String carKey) throws Exception {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT ID FROM CARS WHERE CAR_KEY=\"" + carKey + "\";");
      rs.first();
      return rs.getInt("ID");
  }

  private void executeUpdate(Connection conn, int carId, DataEntry entry) throws Exception {
    Statement stmt = conn.createStatement();
    String values = carId + "," + entry.lon + "," + entry.lat + "," + entry.speed + "," + entry.timestamp;
    stmt.executeUpdate("INSERT INTO CAR_POSITIONS (CAR_ID, LON, LAT, SPEED, DATE_CREATE) VALUES (" + values + ");");
  }
}
