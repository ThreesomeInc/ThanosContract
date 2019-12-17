package provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class Consumer0ProviderSchema1Test {

    private static final String HOST = "127.0.0.1";
    Socket socket;
    Server server;

    @Before
    public void setUp() throws Exception {
        Server server = new Server(54321);
        server.init();

        socket = new Socket(HOST, 54321);
        server.start();

    }

    @After
    public void tearDown() throws Exception {
        if (server != null)
            server.stop();
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