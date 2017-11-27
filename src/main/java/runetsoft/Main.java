package runetsoft;

import runetsoft.persistence.DBWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Convert2Lambda")
public class Main {
  private static final int DEFAULT_PORT = 8089;
  private static final String DEFAULT_DB_URL = "jdbc:h2:mem:test";
  public static final Logger LOG = Logger.getLogger("EDDS");

  public static void main(String[] args) throws Exception {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    int port;
    String dbUrl, dbUsername, dbPassword;
    if (args != null && args.length >= 4) {
      port = Integer.parseInt(args[0]);
      dbUrl = args[1];
      dbUsername = args[2];
      dbPassword = args[3];
    } else {
      port = DEFAULT_PORT;
      dbUrl = DEFAULT_DB_URL;
      dbUsername = null;
      dbPassword = null;
    }
    LOG.log(Level.INFO, "Listening port : " + port);
    final ServerSocket socket = new ServerSocket(port);
    final DBWriter writer = new DBWriter(dbUrl, dbUsername, dbPassword);
    while (!Thread.currentThread().isInterrupted()) {
      final Socket client = socket.accept();
      System.out.println("ACCEPTED!");
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            final MessageProcessor processor = new MessageProcessor(writer);
            InputStream in = client.getInputStream();
            String msg = readMessage(in);
            while (msg != null) {
              String reply = processor.process(msg);
              LOG.log(Level.INFO, "Message : " + msg + "\nReply : " + reply);
              if (reply != null) {
                client.getOutputStream().write(reply.getBytes(StandardCharsets.UTF_8));
                client.getOutputStream().flush();
              }
              msg = readMessage(in);
            }
            client.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }).start();
    }
  }

  private static String readMessage(InputStream in) throws IOException {
    byte[] buff = new byte[8];
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int numRead;
    while ((numRead = in.read(buff)) != -1) {
      out.write(buff, 0, numRead);
      String msg = new String(out.toByteArray(), StandardCharsets.UTF_8);
      if (msg.endsWith("\r\n")) {
        return msg;
      }
    }
    return null;
  }
}
