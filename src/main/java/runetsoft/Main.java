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
  private static final int DEFAULT_PORT = 20332;
  public static final Logger LOG = Logger.getLogger("EDDS");

  public static void main(String[] args) throws Exception {
    if (args == null || args.length != 1) {
      System.out.println("Usage : java -jar EDDS.jar [db_connection_url]");
      System.exit(1);
    }

    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    String dbUrl = args[0];
    LOG.log(Level.INFO, "Listening port : " + DEFAULT_PORT);
    final ServerSocket socket = new ServerSocket(DEFAULT_PORT);
    final DBWriter writer = new DBWriter(dbUrl);
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
