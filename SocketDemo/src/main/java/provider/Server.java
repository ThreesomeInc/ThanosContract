package provider;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private ServerSocket ss;
    private Socket socket;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void init() throws IOException {
        ss = new ServerSocket(port);
        System.out.println("Socket server started @ " + ss.getLocalPort());
    }

    public void start() throws IOException {
        socket = ss.accept();
        System.out.println("Consumer: " + socket.getInetAddress().getLocalHost() + " connected");
        process();
    }

    private void process() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input = br.readLine();
        System.out.println("Received input ：" + input);

        String result = new ProviderHandler().handle(input);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(result + "\n");
        bw.flush();
    }

    public void stop() throws IOException {
        socket.close();
        ss.close();
    }

    public int getPort() {
        return port;
    }
}
