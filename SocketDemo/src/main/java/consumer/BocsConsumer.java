package consumer;

import java.io.*;
import java.net.Socket;

public class BocsConsumer {

    private static final int PORT = 52850;
    private static final String HOST = "127.0.0.1";
    private static final String CRLF = System.lineSeparator();

    private static final String REQUEST = "00123456789005Di1pG2h0UsrmsJXCTIa999123456789012345678901234567890111234567890123456789012345678902212345678901234567890123456789033          000100000011111122222299999999940123456789012345671          ";

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT)) {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(REQUEST + CRLF);
            bw.flush();
            socket.shutdownOutput();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = br.readLine();
            System.out.println("ProviderMain responsed：" + response);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
