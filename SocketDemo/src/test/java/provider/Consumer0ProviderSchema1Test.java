package provider;

import org.junit.After;
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
        {
            //Keep waiting
        }
        while (getPort() == 0) ;

        socket = new Socket(HOST, getPort());


    }

    @After
    public void tearDown() throws Exception {
        if (socket != null)
            socket.close();
    }

    @Test
    public void test_consumer0_provider_schema1() throws IOException {
        final String response = sendRequestAndGetResponse("0AA");
        assertEquals("20020191212", response);
    }


    String sendRequestAndGetResponse(String request) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(request + "\n");
        bw.flush();
        socket.shutdownOutput();
        //读取服务器返回的消息
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return br.readLine();
    }
}