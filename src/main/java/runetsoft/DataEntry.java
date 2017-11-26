package runetsoft;

public class DataEntry {
  public final double lon;
  public final double lat;
  public final int speed;
  public final String timestamp;

  DataEntry(String[] params) {
    if (params[0].length() == 6 && params[1].length() == 6) {
      String date = "20" + params[0].substring(4) + "-" + params[0].substring(2, 4) + "-" + params[0].substring(0, 2);
      String time = params[1].substring(0, 2) + ":" + params[1].substring(2, 4) + ":" + params[1].substring(4);
      timestamp = "\"" + date + " " + time + "\"";
    } else {
      timestamp = "NOW()";
    }
    int lonSign = "N".equals(params[3]) ? 1 : -1;
    lon = Double.parseDouble(params[2]) * lonSign;
    int latSign = "E".equals(params[5]) ? 1 : -1;
    lat = Double.parseDouble(params[4]) * latSign;
    speed = Integer.parseInt(params[6]);
  }
}
