import java.io.*;
import java.net.Socket;

public class Consumer0 {

    private static final int PORT = 8888;
    private static final String HOST = "127.0.0.1";

    private static final String REQUEST = "0AA";

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(REQUEST + "\n");
            bw.flush();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = br.readLine();
            System.out.println("Provider responsed：" + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
