package runetsoft;

import runetsoft.persistence.DBWriter;

class MessageProcessor {
  private static final String PING_RESP = "#AP#\r\n";
  private static final String LOGIN_RESP_SUCCESS = "#AL#1\r\n";
  private static final String LOGIN_RESP_FAIL = "#AL#0\r\n";
  private static final String DATA_SUCCESS = "#AD#1\r\n";
  private static final String DATA_FAIL = "#AD#-1\r\n";
  private static final String BLACK_BOX_FAIL = "#AB#0\r\n";

  private final DBWriter myWriter;
  private String myCarKey;

  MessageProcessor(DBWriter writer) {
    myWriter = writer;
  }

  String process(String msg) {
    if (!msg.startsWith("#")) {
      return null;
    }
    String id = msg.substring(1, msg.indexOf("#", 1));
    switch (id) {
      case "P":
        return PING_RESP;
      case "L":
        String[] data = msg.substring(3).split(";");
        if (data.length >= 2) {
          myCarKey = data[1];
          return LOGIN_RESP_SUCCESS;
        }
        return LOGIN_RESP_FAIL;
      case "D":
        if (myCarKey == null) {
          return DATA_FAIL;
        }
        myWriter.write(myCarKey, new DataEntry(msg.substring(3).split(";")));
        return DATA_SUCCESS;
      case "B":
        if (myCarKey == null) {
          return BLACK_BOX_FAIL;
        }
        String[] packets = msg.substring(3).split("\\|");
        for (int i = 0; i < packets.length - 1; i++) {
          myWriter.write(myCarKey, new DataEntry(packets[i].split(";")));
        }
        return "#AB#" + (packets.length - 1) + "\r\n";
      default:
        return "#A" + id + "#1\r\n";
    }
  }

}
