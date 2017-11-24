package runetsoft;

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
  static final int DEFAULT_PORT = 8089;
  private static final Logger LOG = Logger.getLogger("EDDS");

  public static void main(String[] args) throws Exception {
    int port;
    if (args != null && args.length > 0) {
      port = Integer.parseInt(args[0]);
    } else {
      port = DEFAULT_PORT;
    }
    LOG.log(Level.INFO, "Listening port : " + port);
    final ServerSocket socket = new ServerSocket(port);
    final WialonIPSParser parser = new WialonIPSParser();
    while (!Thread.currentThread().isInterrupted()) {
      final Socket client = socket.accept();
      System.out.println("ACCEPTED!");
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            InputStream in = client.getInputStream();
            String msg = readMessage(in);
            while (msg != null) {
              LOG.log(Level.INFO, "Message received : " + msg);
              String reply = parser.apply(msg);
              if (reply != null) {
                client.getOutputStream().write(reply.getBytes(StandardCharsets.UTF_8));
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
