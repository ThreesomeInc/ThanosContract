package provider;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class Consumer0ProviderSchema1Test extends IntegrationBase {

    private static final String HOST = "127.0.0.1";
    Socket socket;

    @Before
    public void setUp() throws Exception {
        socket = new Socket(HOST, port);
    }

    @Test
    public void test_case_1() throws IOException {
        final String response = sendRequestAndGetResponse("0AA");
        assertEquals("", response);
    }


    String sendRequestAndGetResponse(String request) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(request + "\n");
        bw.flush();
        //读取服务器返回的消息
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return br.readLine();
    }
}