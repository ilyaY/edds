package runetsoft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
//import java.util.Timer;
//import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Convert2Lambda")
public class Main {
  private static final int DEFAULT_PORT = 8089;

  public static void main(String[] args) throws Exception {
//    new Timer(true).schedule(new TimerTask() {
//      @Override
//      public void run() {
//        runTestClient();
//      }
//    }, 1000, 1000);

    int port;
    if (args != null && args.length > 0) {
      port = Integer.parseInt(args[0]);
    } else {
      port = DEFAULT_PORT;
    }
    Logger.getLogger("EDDS").log(Level.INFO, "Listening port : " + port);
    ServerSocket socket = new ServerSocket(port);
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
            Logger.getLogger("EDDS").log(Level.INFO, "Message received : " + msg);
            client.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }).start();
    }
  }

  @SuppressWarnings("unused")
  private static void runTestClient() {
    try {
      Socket client = new Socket("127.0.0.1", DEFAULT_PORT);
      byte[] bytes = "test".getBytes(StandardCharsets.UTF_8);
      client.getOutputStream().write(bytes);
      client.close();
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
