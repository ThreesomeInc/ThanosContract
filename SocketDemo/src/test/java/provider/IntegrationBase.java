package provider;

import org.junit.After;
import org.junit.BeforeClass;

import java.io.IOException;

public class IntegrationBase {

    public static int port;
    public static Server server;

    @BeforeClass
    public static void setup() throws Exception {

//        InetAddress locIP = InetAddress.getByName("127.0.0.1");
        server = new Server(0);

        Thread serverThread = new Thread(() -> {
            try {
                server.init();
                server.start();
                port = server.getPort();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }
}
