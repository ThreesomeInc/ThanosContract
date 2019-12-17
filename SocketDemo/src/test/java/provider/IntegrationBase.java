package provider;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public class IntegrationBase {

    static int port;
    static Server server;

    public static int getPort() {
        return port;
    }

    @Before
    public void setup() throws Exception {

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
        if (server != null)
            server.stop();
    }
}
