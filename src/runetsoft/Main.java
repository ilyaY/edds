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
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            InputStream in = client.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buff = new byte[256];
            int read;
            while ((read = in.read(buff)) != -1) {
              out.write(buff, 0, read);
            }
            String msg = new String(out.toByteArray(), StandardCharsets.UTF_8);
            LOG.log(Level.INFO, "Message received : " + msg);
            String reply = parser.apply(msg);
            if (reply != null) {
              client.getOutputStream().write(reply.getBytes(StandardCharsets.UTF_8));
            }
            client.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }).start();
    }
  }
}
