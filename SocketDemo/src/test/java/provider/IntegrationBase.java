package provider;

import org.jooq.lambda.Unchecked;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

public class IntegrationBase {

    static int port;
    static Server server;

    public static int getPort() {
        return port;
    }

    @BeforeClass
    public static void beforeAll() throws Exception {

        server = new Server(0);

        Thread serverThread = new Thread(Unchecked.runnable(() -> {
            try {
                server.init();
                server.start();
                port = server.getPort();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        serverThread.start();
    }

    @AfterClass
    public static void afterAll() throws Exception {
        if (server.getSs() != null)
            server.stop();
    }
}
