package provider;

import java.io.IOException;

public class ProviderMain {

    private static final int PORT = 8081;

    public static void main(String[] args) throws IOException {

        final Server server = new Server(PORT);

        try {
            server.init();
            while (true) {
                server.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.stop();
            System.exit(0);
        }

    }

}
