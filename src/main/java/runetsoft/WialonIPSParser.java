package runetsoft;

public class WialonIPSParser {
  String apply(String msg) {
    if (!msg.startsWith("#")) {
      return null;
    }
    String id = msg.substring(1, msg.indexOf("#", 1));
    switch (id) {
      case "P":
        return "#AP#\r\n";
      case "B":
        int count = msg.substring(3).split("\\|").length;
        return "#AB#" + (count - 1) + "\r\n";
      default:
        return "#A" + id + "#1\r\n";
    }
  }

  public static void main(String[] args) {
    WialonIPSParser parser = new WialonIPSParser();
    System.out.println(parser.apply("#L#2.0;imei;N/A;BB2B"));
    System.out.println(parser.apply("#B#date;time;lat1;lat2;lon1;lon2;speed;course;height;sats|" +
        "date;time;lat1;lat2;lon1;lon2;speed;course;height;sats|" +
        "date;time;lat1;lat2;lon1;lon2;speed;course;height;sats|crc16\r\n"));
    System.out.println(parser.apply("#M#msg;crc16\r\n"));
    System.out.println(parser.apply("#P#\r\n"));
  }
}
