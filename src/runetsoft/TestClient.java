package runetsoft;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import static runetsoft.Main.DEFAULT_PORT;

public class TestClient {
  public static void main(String[] args) {
    final AtomicInteger count = new AtomicInteger();
    new Timer(false).schedule(new TimerTask() {
      @Override
      public void run() {
        runTestClient(count.incrementAndGet());
      }
    }, 1000, 1000);
  }

  private static void runTestClient(int i) {
    try {
      Socket client = new Socket("127.0.0.1", DEFAULT_PORT);
      byte[] bytes = ("test-" + i).getBytes(StandardCharsets.UTF_8);
      client.getOutputStream().write(bytes);
      client.close();
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
