import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Provider {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Provider up @ " + PORT);

            while (true) {
                Socket socket = ss.accept();
                System.out.println("Consumer: " + socket.getInetAddress().getLocalHost() + " connected");

                //Read from cusumer
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String input = br.readLine();
                System.out.println("Received input ：" + input);

                String result = handle(input);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(result + "\n");
                bw.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String handle(String input) throws IOException {
        if (input.startsWith("0")) {
            return "20020191212";
        } else {
            return "40420191212";
        }
    }
}
